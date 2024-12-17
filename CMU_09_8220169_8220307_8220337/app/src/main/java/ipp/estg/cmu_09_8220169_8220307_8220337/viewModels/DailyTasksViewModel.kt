package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences.UserPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.DailyTasksRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.getImageFromFile
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.saveImageToFile
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.saveImageToGallery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class DailyTasksViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dailyTasksRepository: DailyTasksRepository =
        DailyTasksRepository(LocalDatabase.getDatabase(application).dailyTaskCompletionDao)
    private val userPreferencesRepository = UserPreferencesRepository(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap = _imageBitmap.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak = _streak.asStateFlow()

    var todaysTasks: LiveData<DailyTasks> = MutableLiveData()

    fun loadTodayTasks() {
        val userId = userPreferencesRepository.getCurrentUserId()
        viewModelScope.launch {
            _isLoading.value = true
            todaysTasks = dailyTasksRepository.getTodayTasksLiveData(userId)
            loadTodaysProgressPicture()
            _isLoading.value = false
        }
    }

    fun updateDailyStreak() {
        viewModelScope.launch {
            val streak = dailyTasksRepository.getStreak()
            _streak.value = streak
        }
    }

    fun setTasksValue(dailyTasks: DailyTasks) {
        val userId = userPreferencesRepository.getCurrentUserId()

        // insert on room
        viewModelScope.launch {
            dailyTasksRepository.insertTasks(
                userId,
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

    fun updateProgressPicture(bitmap: Bitmap) {
        _imageBitmap.value = bitmap

        // Guardar a imagem num ficheiro
        val fileAbsolutePath = saveImageToFile(getApplication(), bitmap)

        // Atualizar a task
        val dailyTasks = todaysTasks.value
        if (dailyTasks != null) {
            val newTasks = dailyTasks.copy(takeProgressPicture = fileAbsolutePath)
            setTasksValue(newTasks)
        }
    }

    private fun loadTodaysProgressPicture() {
        viewModelScope.launch {
            // Load da fotografia de hoje, se existir
            val todayProgressPicturePath = dailyTasksRepository.getTodaysProgressPicture();
            if (todayProgressPicturePath.isNotEmpty()) {
                val image = getImageFromFile(todayProgressPicturePath)
                _imageBitmap.value = image
            }
        }
    }

    fun saveProgressPitureToGallery(): String? {
        val bitmap = imageBitmap.value ?: return null

        val currentDate = LocalDate.now().toString()
        val imageName = "progress_picture_${currentDate}.png"
        val imageDescription = "Progress picture taken on $currentDate"

        return saveImageToGallery(getApplication(), bitmap, imageName, imageDescription)
    }

}