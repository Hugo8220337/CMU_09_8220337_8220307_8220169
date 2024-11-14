package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
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
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.DailyTasksRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.IDailyTasksRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.IQuotesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.QuotesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.RemoteApis
import ipp.estg.cmu_09_8220169_8220307_8220337.services.DailyRemeinderService
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    val settingsPreferencesRepository: SettingsPreferencesRepository =
        SettingsPreferencesRepository(application)

    var state: ScreenState by mutableStateOf(ScreenState())

    val dailyTasksRepository: IDailyTasksRepository
    var quotesRepository: IQuotesRepository

    var tasksLiveData: LiveData<DailyTasks>
    var streak: Int = 0
    var dailyQuote: String = ""



    init {

        buildForegroundDailyRemeinderNotifications()


        val quotesApi = RemoteApis.getQuotesApi()
        val dbDao = LocalDatabase.getDatabase(application)

        // Inicializar repositórios
        dailyTasksRepository = DailyTasksRepository(dbDao.dailyTaskCompletionDao)
        quotesRepository = QuotesRepository(quotesApi, dbDao.quotesDao)

        // Obtem as tasks de hoje
        tasksLiveData = dailyTasksRepository.getTodayTasks()

        // Lança uma coroutine para obter o daily streak e a daily quote de forma assíncrona
        viewModelScope.launch {
            streak = dailyTasksRepository.getStreak()
            //dailyQuote = quotesRepository.getTodaysQuote().quote
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
            dailyTasksRepository.insertTasks(
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

