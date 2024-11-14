package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.preferences.SettingsPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.room.repositories.DailyTasksLocalRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.room.repositories.QuoteRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.services.DailyRemeinderService
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    val settingsPreferencesRepository: SettingsPreferencesRepository =
        SettingsPreferencesRepository(application)

    var state: ScreenState by mutableStateOf(ScreenState())

    val dailyTasksLocalRepository: DailyTasksLocalRepository
    var quoteRepository: QuoteRepository

    var tasksLiveData: LiveData<DailyTasks>
    var streak: Int = 0
    var dailyQuote: String = ""



    init {

        buildForegroundDailyRemeinderNotifications()


        // inicializa as tasks com a informação em cache no Room e o repositório de citações
        val dbDao = LocalDatabase.getDatabase(application)
        dailyTasksLocalRepository = DailyTasksLocalRepository(dbDao.dailyTaskCompletionDao)
        quoteRepository = QuoteRepository(dbDao.quotesDao)

        tasksLiveData = dailyTasksLocalRepository.getTodayTasks()

        // Lança uma coroutine para obter o streak de forma assíncrona
        viewModelScope.launch {
            // Atualiza streak dentro da coroutine
            streak = dailyTasksLocalRepository.getStreak()
            // Obtem a citação do dia
            //dailyQuote = quoteRepository.getTodaysQuote().quote
        }

    }


    /**
     * Começa o DailyRemeinderService que vai mandar notificações com a app fechada
     * quando o utilizador não realiza o treino
     */
    private fun buildForegroundDailyRemeinderNotifications() {
        val channel = NotificationChannel(
            DailyRemeinderService.TIMER_SERVICE_NOTIICATION_CHANNEL_ID,
            DailyRemeinderService.TIMER_SERVICE_NOTIICATION_CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Lembretes diários para exercícios"
            enableLights(true)
            enableVibration(true)
        }

        val application = this.getApplication<Application>()
        val notificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Start the service
        val serviceIntent = Intent(application, DailyRemeinderService::class.java).apply {
            action = DailyRemeinderService.Actions.START.toString()
        }
        application.startForegroundService(serviceIntent)


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val channel = NotificationChannel(
//                DailyRemeinderService.TIMER_SERVICE_NOTIICATION_CHANNEL_ID,
//                "Workout Notification",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            val application = getApplication<Application>()
//            val notificationManager =
//                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//
//            // start Service
//            val serviceIntent = Intent(application, DailyRemeinderService::class.java).also {
//                it.action = DailyRemeinderService.Actions.START.toString()
//                application.startForegroundService(it)
//            }
//        }


    }

    fun setTasksValue(dailyTasks: DailyTasks) {
        //tasksLiveData.postValue(dailyTasks)

        // insert on room
        viewModelScope.launch {
            dailyTasksLocalRepository.insertTasks(
                dailyTasks.gallonOfWater,
                dailyTasks.twoWorkouts,
                dailyTasks.followDiet,
                dailyTasks.readTenPages,
                dailyTasks.takeProgressPicture
            )
        }
    }


    // Função para atualizar a imagem
    fun updateProgressPicture(bitmap: Bitmap) {
        state = state.copy(imageBitmap = bitmap)
    }

    fun getProgressPicture(): Bitmap? {
        return state.imageBitmap
    }

    fun getError(): String? {
        return state.error
    }


    data class ScreenState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val hasCameraPermission: Boolean = false,
        val imageBitmap: Bitmap? = null,
    )
}

