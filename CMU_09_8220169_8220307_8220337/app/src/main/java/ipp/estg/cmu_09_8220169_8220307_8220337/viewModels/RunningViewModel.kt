package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.annotation.SuppressLint
import android.app.Application
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RunningViewModel(
    application: Application
) : AndroidViewModel(application), SensorEventListener {

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


    val stepCounter = mutableIntStateOf(0)
    var isRunning by mutableStateOf(false) // Track if the run is active

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    // CancellationTokenSource to cancel the location request
    private val cancellationTokenSource = CancellationTokenSource()


    private lateinit var locationCallback: LocationCallback
    private val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000 // Location update interval in milliseconds (5 seconds)
    )
        .setMinUpdateIntervalMillis(2000) // Minimum interval (2 seconds)
        .build()


    init {
        // Initialize sensor if available
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        } ?: run {
            // Handle case where sensor is not available (could log or show a message)
            // Log.d("RunningViewModel", "Step counter sensor not available.")
        }

        setupLocationCallback() // Setup location callback
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                _currentLocation.value = locationResult.lastLocation
            }
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission") // Ensure permissions are checked before calling
    fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    fun getBatteryLevel(): Int {
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER && isRunning) { // só conta passos se estiver a correr
                // Update step count based on the step counter sensor event
                stepCounter.intValue += 1
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this implementation, left unimplemented
    }

    /**
     * Is called when the ViewModel is no longer used and is about to be destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        // Unregister sensor listener when ViewModel is cleared
        sensorManager.unregisterListener(this)
    }

}