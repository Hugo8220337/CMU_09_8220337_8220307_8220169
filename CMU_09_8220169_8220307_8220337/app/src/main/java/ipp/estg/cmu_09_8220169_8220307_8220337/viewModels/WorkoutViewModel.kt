package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.ExerciseItemDataResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.IWorkoutRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.WorkoutRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.RemoteApis
import ipp.estg.cmu_09_8220169_8220307_8220337.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Resource
import kotlinx.coroutines.launch
import java.util.Locale

class WorkoutViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val workoutRepository: IWorkoutRepository = WorkoutRepository(
        exerciseDbApi = RemoteApis.getExerciseDbApi(),
        workoutDao = LocalDatabase.getDatabase(application).workoutDao
    )


    var state by mutableStateOf(ScreenState())
        private set

    fun generateWorkout(bodyParts: List<String>) {
        viewModelScope.launch {
            state = state.copy(isGeneratingWorkout = true)

            // load exercises from the repository
            val exercises = workoutRepository.getExercisesByBodyParts(
                bodyParts = bodyParts,
                limit = 10,
                offset = 0
            )

            // store exercises on the state
            state = state.copy(workout = exercises)

            state = state.copy(isGeneratingWorkout = false)
        }
    }

    data class ScreenState(
        val isLoading: Boolean = false,
        val isGeneratingWorkout: Boolean = false,
        val error: String? = null,
        val workout: List<ExerciseItemDataResponse> = emptyList()
    )
}

