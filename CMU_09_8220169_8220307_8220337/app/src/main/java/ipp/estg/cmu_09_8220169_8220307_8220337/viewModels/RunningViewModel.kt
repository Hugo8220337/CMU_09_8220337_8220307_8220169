package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.BatteryManager
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import ipp.estg.cmu_09_8220169_8220307_8220337.services.StepCounterService
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Timer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RunningViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val LOCATION_UPDATE_INTERVAL_MS = 5000L
    private val LOCATION_MIN_UPDATE_INTERVAL_MS = 2000L
    private val ONE_SECOND_MS = 1000L

    private val sensorManager: SensorManager by lazy {
        application.getSystemService(Application.SENSOR_SERVICE) as SensorManager
    }
    private var sensor: Sensor? = null // Step counter sensor

    private val batteryManager: BatteryManager by lazy {
        application.getSystemService(Application.BATTERY_SERVICE) as BatteryManager
    }

    /**
     * FusedLocationProviderClient is used to get the last known location of the device.
     */
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)


    /**
     * MutableStateFlow to hold the current distance, time, and pace of the run.
     */
    private val _distance = MutableStateFlow(0.0) // Distance in kilometers
    val distance: StateFlow<Double> = _distance


    private var startTime: Long? = null // Start time of the run
    private val _time = MutableStateFlow(0) // Time in seconds
    val time: StateFlow<Int> = _time

    private val _pace = MutableStateFlow(0.0) // Pace in minutes/km
    val pace: StateFlow<Double> = _pace

    private val _stepCounter = MutableStateFlow(0)
    val stepCounter = _stepCounter

    var isRunning by mutableStateOf(false) // Track if the run is active

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation

    /**
     * Job to run a timer that increments the time every second.
     */
    private lateinit var locationCallback: LocationCallback
    private val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000 // Location update interval in milliseconds (5 seconds)
    )
        .setMinUpdateIntervalMillis(2000) // Minimum interval (2 seconds)
        .build()

    /**
     * Timer to increment the time every second.
     */
    private val timer = Timer { increment ->
        _time.value += increment
    }


    init {
        setupLocationAndStatsCallback() // Setup location callback
    }

    fun startRun() {
        isRunning = true
        startTimer()
        startStepCounterService()
        StepCounterService.onStepDetected = { steps ->
            updateStepsFromService(steps)
        }
        startLocationAndStatsUpdates()
    }

    fun stopRun() {
        isRunning = false
        stopStepCounterService()
        stopTimer()
        stopLocationUpdates()
    }

    private fun startStepCounterService() {
        val intent = Intent(getApplication(), StepCounterService::class.java)
        getApplication<Application>().startService(intent)
    }

    private fun stopStepCounterService() {
        val intent = Intent(getApplication(), StepCounterService::class.java)
        getApplication<Application>().stopService(intent)
    }

    /**
     * Setup the location callback to get the last known location of the device.
     */
    private fun setupLocationAndStatsCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                _currentLocation.value = locationResult.lastLocation // update location
                updateStats(locationResult.locations) // update stats
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission") // Ensure permissions are checked before calling
    fun startLocationAndStatsUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    fun getBatteryLevel(): Int {
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }


    private fun updateStepsFromService(steps: Int) {
        _stepCounter.value = steps // Atualiza o contador de passos
    }


    private fun updateStats(locationList: List<Location>) {
        // Calculate distance from locationList
        _distance.value = calculateDistance(locationList)

        val elapsedSeconds = getElapsedTime() // Calculate elapsed seconds
        _time.value = elapsedSeconds.toInt()

        // Calculate pace (minutes/km)
        _pace.value = if (_distance.value > 0) {
            (elapsedSeconds / 60) / _distance.value
        } else {
            0.0
        }
    }

    private fun getElapsedTime(): Long {
        val currentTimestamp = System.currentTimeMillis()
        if (startTime == null) {
            startTime = currentTimestamp // Initialize the start time
        }
        return (currentTimestamp - startTime!!) / ONE_SECOND_MS
    }

    private fun calculateDistance(locationList: List<Location>): Double {
        if (locationList.size < 2) return 0.0

        var totalDistance = 0.0
        for (i in 1 until locationList.size) {
            val start = locationList[i - 1]
            val end = locationList[i]
            totalDistance += start.distanceTo(end) // distanceTo returns distance in meters
        }
        return totalDistance / 1000 // Convert to kilometers
    }


    fun startTimer() = timer.start(viewModelScope)
    fun stopTimer() = timer.stop()
    fun resetTimer() {
        _time.value = 0 // Reset the timer to 0
    }

    /**
     * Is called when the ViewModel is no longer used and is about to be destroyed.
     */
    override fun onCleared() {
        super.onCleared()
    }

}