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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences.SettingsPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.DailyTasksRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.QuotesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.RemoteApis
import ipp.estg.cmu_09_8220169_8220307_8220337.services.DailyRemeinderService
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.getImageFromFile
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.saveImageToFile
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.saveImageToGallery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val dailyTasksRepository: DailyTasksRepository =
        DailyTasksRepository(LocalDatabase.getDatabase(application).dailyTaskCompletionDao)

    private var quotesRepository: QuotesRepository =
        QuotesRepository(
            RemoteApis.getQuotesApi(),
            LocalDatabase.getDatabase(application).quotesDao
        )


    var state: ScreenState by mutableStateOf(ScreenState())


    private val _dailyQuote = MutableStateFlow("")
    val dailyQuote = _dailyQuote.asStateFlow()

    var dailyTasks: LiveData<DailyTasks> = MutableLiveData()

    private var _allDailyTasks = MutableStateFlow<List<DailyTasks>>(emptyList())
    val allDailyTasks = _allDailyTasks.asStateFlow()

    init {
        // Configura o idioma baseado na preferência guardada
        val savedLanguage = settingsPreferencesRepository.getLanguagePreference()
        settingsPreferencesRepository.updateLocale(application, savedLanguage)
    }

    fun loadTodaysProgressPicture() {
        viewModelScope.launch {
            // Load da fotografia de hoje, se existir
            val todayProgressPicturePath = dailyTasksRepository.getTodaysProgressPicture();
            if (todayProgressPicturePath.isNotEmpty()) {
                val image = getImageFromFile(todayProgressPicturePath)
                state = state.copy(imageBitmap = image)
            }
        }
    }

    fun loadTodayTasks() {
        viewModelScope.launch {

            dailyTasks = dailyTasksRepository.getTodayTasksLiveData()
        }
    }

    fun updateDailyStreak() {
        viewModelScope.launch {
            val streak = dailyTasksRepository.getStreak()
            state = state.copy(streak = streak)
        }
    }

    fun loadDailyQuote() {
        viewModelScope.launch {
            val dailyQuote = quotesRepository.getTodaysQuote().quote
            _dailyQuote.value = dailyQuote
        }
    }

    fun loadAllTasks() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            _allDailyTasks.value = dailyTasksRepository.getAllTasks()
            state = state.copy(isLoading = false)
        }
    }


    fun buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val application = getApplication<Application>()
            // start Service
            Intent(application, DailyRemeinderService::class.java).also {
                application.startService(it)
            }
        }
    }

    fun stopNotificationService() {
        Intent(getApplication(), DailyRemeinderService::class.java).also {
            getApplication<Application>().stopService(it)
        }
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
        val dailyTasks = dailyTasks.value
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
        val imageBitmap: Bitmap? = null,
    )
}