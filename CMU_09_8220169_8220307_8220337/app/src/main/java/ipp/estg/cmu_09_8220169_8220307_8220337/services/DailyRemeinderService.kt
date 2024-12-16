package ipp.estg.cmu_09_8220169_8220307_8220337.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences.SettingsPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.DailyTasksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyRemeinderService : Service() {
    private val CHANNEL_ID = "DailyReminders"
    private val CHANNEL_NAME = "Daily Reminders"
    private val FOREGROUND_ID = 1
    private val NOTIFICATION_ID = 2
    private val NOTIFICATION_INTERVAL = 3600000L // 1 hora em milissegundos


    private lateinit var dailyTasksRepository: DailyTasksRepository
    private val settingsRepository: SettingsPreferencesRepository by lazy {
        SettingsPreferencesRepository(applicationContext)
    }

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
            .setContentTitle("Serviço de notificações em foreground")
            .setContentText("Não perca o foco, continue a sua jornada.\nTu és uma alface do Lidl!")
            .build()

        startForeground(FOREGROUND_ID, notification)
    }


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        // Initialize the dailyTasksRepository
        dailyTasksRepository = DailyTasksRepository(
            LocalDatabase.getDatabase(applicationContext).dailyTaskCompletionDao
        )
    }


    private fun showNotification() {
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_75hard_challange_logo_foreground)
            .setContentTitle("Não esqueça do desafio")
            .setContentText("Não perca o foco, continue a sua jornada.\nTu és uma alface do Lidl!")
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun areTodaysTasksCompleted(): Boolean {
        return dailyTasksRepository.areTodaysTasksDone()
    }

    private fun notificationLooper() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val notificationPreference = settingsRepository.getNotificationsPreference()
                CoroutineScope(Dispatchers.IO).launch {
                    if (!areTodaysTasksCompleted() && notificationPreference) {
                        showNotification()
                    }
                }
                handler.postDelayed(this, NOTIFICATION_INTERVAL)
            }
        }
        handler.post(runnable)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startServiceInForeground()

        notificationLooper()

        return START_STICKY // Ensures the service restarts if terminated by the system
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}