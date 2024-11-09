package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
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
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    val settingsPreferencesRepository: SettingsPreferencesRepository = SettingsPreferencesRepository(application)

    var state: ScreenState by mutableStateOf(ScreenState())
    val dailyTasksLocalRepository: DailyTasksLocalRepository
    var tasksLiveData: LiveData<DailyTasks>


    init {
        val dbDao = LocalDatabase.getDatabase(application).dailyTaskCompletionDao
        dailyTasksLocalRepository = DailyTasksLocalRepository( dbDao )

        tasksLiveData = dailyTasksLocalRepository.getTodayTasks()
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

