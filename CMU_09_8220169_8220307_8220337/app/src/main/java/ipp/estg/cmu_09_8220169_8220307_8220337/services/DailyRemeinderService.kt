package ipp.estg.cmu_09_8220169_8220307_8220337.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.services.workers.DailyReminderWorker
import java.util.concurrent.TimeUnit

class DailyRemeinderService : Service() {
    companion object {
        const val TIMER_SERVICE_NOTIFICATION_ID = 1
        const val TIMER_SERVICE_NOTIICATION_CHANNEL_ID = "DailyReminders"
    }

    private lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()

        // the workManager channel creates when the Service is created
        workManager = WorkManager.getInstance(applicationContext)
        schedulePeriodicCheck()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> {
                cancelPeriodicCheck()
                stopSelf()
            }
        }

        // If the mobile device does not have enough space, the process is saved so that it can be executed later, when space is available.
//        return START_STICKY

        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("ForegroundServiceType")
    private fun start() {
        val notification = Notification.Builder(this, TIMER_SERVICE_NOTIICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_75hard_challange_logo_foreground)
            .setContentTitle("Não esqueça do desafio")
            .setContentText("JUST DO IT")
            .build()

        startForeground(TIMER_SERVICE_NOTIFICATION_ID, notification)
    }

    private fun doTask() {

    }

    private fun updateNotification(data: String) {

    }


    private fun schedulePeriodicCheck() {
        // Check every 2 hours if exercises are done
        val periodicWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            10, TimeUnit.SECONDS,
            15, TimeUnit.SECONDS
        ).build()

//        val periodicWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
//            2, TimeUnit.HOURS,
//            15, TimeUnit.MINUTES
//        ).build()

        workManager.enqueueUniquePeriodicWork(
            "exercise_check",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
    }

    private fun cancelPeriodicCheck() {
        workManager.cancelUniqueWork("exercise_check")
    }


    override fun onBind(p0: Intent?): IBinder? = null // Don't need it for now


    enum class Actions {
        START, STOP
    }
}