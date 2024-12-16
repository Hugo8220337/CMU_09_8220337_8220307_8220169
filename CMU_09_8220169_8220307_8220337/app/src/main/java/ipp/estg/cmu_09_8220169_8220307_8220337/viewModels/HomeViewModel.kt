package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences.SettingsPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences.UserPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.DailyTasksRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.services.DailyRemeinderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    /**
     * Repositórios
     */
    val settingsPreferencesRepository: SettingsPreferencesRepository =
        SettingsPreferencesRepository(application)

    val userPreferencesRepository = UserPreferencesRepository(application)

    private val dailyTasksRepository: DailyTasksRepository =
        DailyTasksRepository(LocalDatabase.getDatabase(application).dailyTaskCompletionDao)



    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private var _allDailyTasks = MutableStateFlow<List<DailyTasks>>(emptyList())
    val allDailyTasks = _allDailyTasks.asStateFlow()


    init {
        // Configura o idioma baseado na preferência guardada
        val savedLanguage = settingsPreferencesRepository.getLanguagePreference()
        settingsPreferencesRepository.updateLocale(application, savedLanguage)
    }



    fun loadAllUserTasks() {
        val userId = userPreferencesRepository.getCurrentUserId()
        viewModelScope.launch {
            _isLoading.value = true
            _allDailyTasks.value = dailyTasksRepository.getAllUserTasks(userId)
            _isLoading.value = false
        }
    }


    fun startNotificationService() {
        val notificationPreference = settingsPreferencesRepository.getNotificationsPreference()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationPreference) {
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

}