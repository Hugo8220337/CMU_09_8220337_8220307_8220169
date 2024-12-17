package ipp.estg.cmu_09_8220169_8220307_8220337.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import ipp.estg.cmu_09_8220169_8220307_8220337.R

class StepCounterService : Service(), SensorEventListener {
    private val CHANNEL_ID = "StepCounter"
    private val CHANNEL_NAME = "Step Counter"
    private val FOREGROUND_ID = 2

    companion object {
        var onStepDetected: ((Int) -> Unit)? = null // Callback para comunicação com o ViewModel
    }

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var initialStepCount: Int? = null

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NotificationManager::class.java)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startServiceInForeground() {
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_75hard_challange_logo_foreground)
            .setContentTitle(getString(R.string.step_counting_service_foreground_title))
            .setContentText(getString(R.string.step_counting_service_foreground_text))
            .build()

        startForeground(FOREGROUND_ID, notification)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        } ?: Log.e("StepCounterService", "Step sensor not available!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startServiceInForeground()

        return START_STICKY
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            if (initialStepCount == null) {
                initialStepCount = totalSteps
            }
            val stepsInSession = totalSteps - (initialStepCount ?: totalSteps)
            Log.d("StepCounterService", "Steps in session: $stepsInSession")  // Log to verify
            onStepDetected?.invoke(stepsInSession)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}