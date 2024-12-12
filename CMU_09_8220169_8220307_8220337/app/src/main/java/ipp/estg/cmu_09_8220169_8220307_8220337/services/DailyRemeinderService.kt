package ipp.estg.cmu_09_8220169_8220307_8220337.services

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.DailyTasksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class DailyRemeinderService : Service() {
    companion object {
        const val TIMER_SERVICE_NOTIFICATION_ID = 1
        const val TIMER_SERVICE_NOTIICATION_CHANNEL_ID = "DailyReminders"
        const val NOTIFICATION_INTERVAL = 10000L // 10 seconds
    }

    private lateinit var dailyTasksRepository: DailyTasksRepository
    private val notificationManager: NotificationManager by lazy {
        getSystemService(NotificationManager::class.java)
    }


    private val serviceScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate() {
        super.onCreate()
        val notification = Notification.Builder(this, TIMER_SERVICE_NOTIICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_75hard_challange_logo_foreground)
            .setContentTitle("Não esqueça do desafio")
            .setContentText("Não perca o foco, continue a sua jornada.\nTu és uma alface do Lidl!")
            .build()

        startForeground(TIMER_SERVICE_NOTIFICATION_ID, notification)

        // Initialize the dailyTasksRepository
        dailyTasksRepository = DailyTasksRepository(
            LocalDatabase.getDatabase(applicationContext).dailyTaskCompletionDao
        )
    }

    private fun showNotification() {
        val notification = Notification.Builder(this, TIMER_SERVICE_NOTIICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_75hard_challange_logo_foreground)
            .setContentTitle("Não esqueça do desafio")
            .setContentText("Não perca o foco, continue a sua jornada.\nTu és uma alface do Lidl!")
            .build()

        notificationManager.notify(TIMER_SERVICE_NOTIFICATION_ID, notification)
    }

    private suspend fun areTodaysTasksCompleted(): Boolean {
        return dailyTasksRepository.areTodaysTasksDone()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            while (true) {
                if (!areTodaysTasksCompleted()) {
                    showNotification()
                }
                delay(NOTIFICATION_INTERVAL) // Use delay instead of sleep
            }
        }

        return START_STICKY // Service will be restarted if it is killed by the system
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}