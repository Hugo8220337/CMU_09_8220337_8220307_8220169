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
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.getImageFromFile
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.getImageFromFileWithDate
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.saveImageToFile
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.saveImageToGallery
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    /**
     * Repositórios
     */
    val settingsPreferencesRepository: SettingsPreferencesRepository =
        SettingsPreferencesRepository(application)
    val dailyTasksRepository: IDailyTasksRepository
    var quotesRepository: IQuotesRepository


    /**
     * Informação Mutável
     */
    var state: ScreenState by mutableStateOf(ScreenState())
    var tasksLiveData: LiveData<DailyTasks>


    init {

        buildForegroundDailyRemeinderNotifications()


        val quotesApi = RemoteApis.getQuotesApi()
        val dbDao = LocalDatabase.getDatabase(application)

        // Inicializar repositórios
        dailyTasksRepository = DailyTasksRepository(dbDao.dailyTaskCompletionDao)
        quotesRepository = QuotesRepository(quotesApi, dbDao.quotesDao)

        // Obter as tasks de hoje
        tasksLiveData = dailyTasksRepository.getTodayTasks()
        loadTodaysProgressPicture()

        // Obtem o streak atual
        updateDailyStreak()

        // Obtem a quote do dia
        loadDailyQuote()
    }

    private fun loadTodaysProgressPicture() {
        viewModelScope.launch {
            // Load da fotografia de hoje, se existir
            val todayProgressPicturePath = dailyTasksRepository.getTodaysProgressPicture();
            if (todayProgressPicturePath.isNotEmpty()) {
                val image = getImageFromFile(todayProgressPicturePath)
                state = state.copy(imageBitmap = image)
            }
        }
    }


    private fun updateDailyStreak() {
        viewModelScope.launch {
            val streak = dailyTasksRepository.getStreak()
            state = state.copy(streak = streak)
        }
    }

    private fun loadDailyQuote() {
        viewModelScope.launch {
            val dailyQuote = quotesRepository.getTodaysQuote().quote
            state = state.copy(dailyQuote = dailyQuote)
        }
    }

    /**
     * Começa o DailyRemeinderService que vai mandar notificações com a app fechada
     * quando o utilizador não realiza o treino
     * TODO NÃO ESTÁ A FUNCIONAR
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
        // insert on room
        viewModelScope.launch {
            dailyTasksRepository.insertTasks(
                dailyTasks.gallonOfWater,
                dailyTasks.twoWorkouts,
                dailyTasks.followDiet,
                dailyTasks.readTenPages,
                dailyTasks.takeProgressPicture
            )

            // Atualizar streak sempre que houver alguma alteração nas tasks
            updateDailyStreak()
        }
    }


    // Função para atualizar a imagem
    fun updateProgressPicture(bitmap: Bitmap) {
        state = state.copy(imageBitmap = bitmap)

        // Guardar a imagem num ficheiro
        val fileAbsolutePath = saveImageToFile(getApplication(), bitmap)

        // Atualizar a task
        // TODO newTask está null, aposto  que é por causa do observable
        // Observar as tasks e atualizar a task (Mais um OBSERVE, CORRIGIR ISSO)
        val dailyTasks = tasksLiveData.value
        if (dailyTasks != null) {
            val newTasks = dailyTasks.copy(takeProgressPicture = fileAbsolutePath)
            setTasksValue(newTasks)
        }
    }

    fun saveProgressPitureToGallery(): String? {
        val bitmap = state.imageBitmap ?: return null

        val currentDate = LocalDate.now().toString()
        val imageName = "progress_picture_${currentDate}.png"
        val imageDescription = "Progress picture taken on $currentDate"

        return saveImageToGallery(getApplication(), bitmap, imageName, imageDescription)
    }

    data class ScreenState(
        val isLoading: Boolean = false,
        val streak: Int = 0,
        val error: String? = null,
        val hasCameraPermission: Boolean = false,
        val dailyQuote: String = "",
        val imageBitmap: Bitmap? = null,
    )
}