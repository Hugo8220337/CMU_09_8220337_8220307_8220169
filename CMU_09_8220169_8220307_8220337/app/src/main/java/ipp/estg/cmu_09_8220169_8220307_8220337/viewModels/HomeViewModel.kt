package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import coil3.Bitmap
import ipp.estg.cmu_09_8220169_8220307_8220337.Hard75Application
import ipp.estg.cmu_09_8220169_8220307_8220337.preferences.DailyTasksRepository

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    val dailyTasksRepository = DailyTasksRepository(Hard75Application.appModule.dailyTasksPreferences)

    private var state by mutableStateOf(ScreenState())


    fun getTaskGallonOfWater(): Boolean {
        return dailyTasksRepository.getGallonOfWater()
    }

    fun setTaskGallonOfWater(enabled: Boolean) {
        dailyTasksRepository.setGallonOfWater(enabled)
    }

    fun getTaskTwoWorkouts(): Boolean {
        return dailyTasksRepository.getTwoWorkouts()
    }

    fun setTaskTwoWorkouts(enabled: Boolean) {
        dailyTasksRepository.setTwoWorkouts(enabled)
    }

    fun getTaskFollowDiet(): Boolean {
        return dailyTasksRepository.getFollowDiet()
    }

    fun setTaskFollowDiet(enabled: Boolean) {
        dailyTasksRepository.setFollowDiet(enabled)
    }

    fun getTaskReadTenPages(): Boolean {
        return dailyTasksRepository.getReadTenPages()
    }

    fun setTaskReadTenPages(enabled: Boolean) {
        dailyTasksRepository.setReadTenPages(enabled)
    }

    // Função para atualizar a imagem
    fun updateProgressPicture(bitmap: Bitmap) {
        state = state.copy(imageBitmap = bitmap)

        dailyTasksRepository.setTakeProgressPicture(true)
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

