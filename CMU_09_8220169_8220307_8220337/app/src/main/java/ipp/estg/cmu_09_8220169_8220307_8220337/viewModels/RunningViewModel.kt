package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
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
import com.google.android.gms.maps.model.LatLng
import ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences.UserPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Running
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.RunningRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.services.StepCounterService
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Timer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RunningViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val runningRepository: RunningRepository = RunningRepository(
        runningDao = LocalDatabase.getDatabase(application).runningDao
    )
    private val userPreferencesRepository = UserPreferencesRepository(application)

    // Location and Running Tracking
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    // State Flows
    private val _distance = MutableStateFlow(0.0) // Distance in kilometers
    val distance: StateFlow<Double> = _distance

    private val _time = MutableStateFlow(0) // Time in seconds
    val time: StateFlow<Int> = _time

    private val _path = MutableStateFlow<List<LatLng>>(emptyList())
    val path: StateFlow<List<LatLng>> = _path

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    private val _stepCounter = MutableStateFlow(0)
    val stepCounter = _stepCounter

    var isRunning by mutableStateOf(false)

    // Last known location
    private var lastLocation: Location? = null


    private val _lastRun = MutableStateFlow<Running?>(null)
    val lastRun = _lastRun.asStateFlow()

    private val _runnings = MutableStateFlow<List<Running?>>(emptyList())
    val runnings = _runnings.asStateFlow()

    // Estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Estado de erro
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    /**
     * Timer to increment the time every second.
     */
    private val timer = Timer { increment ->
        _time.value += increment
    }

    // Location callback
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val currentLocation = locationResult.lastLocation ?: return

            _currentLocation.value = currentLocation

            // Calculate distance from last known location
            lastLocation?.let { previous ->
                val distanceInMeters = previous.distanceTo(currentLocation)
                _distance.value += distanceInMeters / 1000.0 // Convert to kilometers

                // Update path
                _path.value += LatLng(currentLocation.latitude, currentLocation.longitude)
            }

            // Update last location
            lastLocation = currentLocation
        }
    }


    // Location request configuration
    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000 // 5 seconds interval
    ).build()

    fun startRun() {
        isRunning = true
        startTimer()
        startLocationTracking()
        startStepCounterService()
        Log.d("RunningViewModel", "Run started")
        StepCounterService.onStepDetected = { steps ->
            _stepCounter.value = steps
            Log.d("RunningViewModel", "Step detected: $steps")
        }
    }

    fun pauseRun() {
        isRunning = false
        stopStepCounterService()
        stopLocationTracking()
        stopTimer()
    }

    fun stopRun() {
        pauseRun()

        // Save run data
        viewModelScope.launch {
            runningRepository.insertRunningWorkout(
                distance.value,
                time.value.toLong(),
                stepCounter.value
            )
        }

        // Reset everything
        resetRunData()
    }

    private fun startTimer() = timer.start(viewModelScope)
    private fun stopTimer() = timer.stop()

    private fun resetRunData() {
        _distance.value = 0.0
        _time.value = 0
        _path.value = emptyList()
        _stepCounter.value = 0
        lastLocation = null
    }

    private fun startStepCounterService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val application = getApplication<Application>()
            Intent(application, StepCounterService::class.java).also {
                application.startService(it)
            }
        }
    }

    private fun stopStepCounterService() {
        Intent(getApplication(), StepCounterService::class.java).also {
            getApplication<Application>().stopService(it)
        }
    }


    private fun startLocationTracking() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        } catch (e: SecurityException) {
            // Handle permission issues
            Log.e("RunningViewModel", "Location permission not granted")
        }
    }

    private fun stopLocationTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    // get all running workouts from Firebase by user ID
    fun getRunningWorkoutsByUserID() {
        _isLoading.value = true

        val userId = userPreferencesRepository.getCurrentUserId()
        viewModelScope.launch {
            try {
                val runninWorkouts = runningRepository.getRunningByUserID(userId)

                if (runninWorkouts.isEmpty()) {
                    _runnings.value = emptyList() // Update to empty list
                } else {
                    _runnings.value = runninWorkouts
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getLastRun() {
        _isLoading.value = true
        
        val userId = userPreferencesRepository.getCurrentUserId()
        viewModelScope.launch {
            try {
                val lastRun = runningRepository.getLastRun(userId)

                _lastRun.value = lastRun
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
}