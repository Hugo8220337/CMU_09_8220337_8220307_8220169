package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ipp.estg.cmu_09_8220169_8220307_8220337.Hard75Application
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.ExerciseItem
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.repositories.ExerciseDbApiRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.room.repositories.WorkoutLocalRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Resource
import kotlinx.coroutines.launch
import java.util.Locale

class WorkoutViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val exerciseDbApiRepository = ExerciseDbApiRepository(Hard75Application.appModule.exerciseDbApi)
    private val workoutLocalRepository = WorkoutLocalRepository(Hard75Application.appModule.workoutDao); // cache


    var state by mutableStateOf(ScreenState())
        private set

    fun generateWorkout(bodyParts: List<String>) {
        state = state.copy(isGeneratingWorkout = true)

        for (bodyPart in bodyParts) {
            exerciseDbApiRepository.getExercisesByBodyPart(
                bodyPart = bodyPart.lowercase(Locale.ROOT),
                limit = 10,
                offset = 0
            ) { result ->
                when (result) {
                    is Resource.Success -> {
                        val exercisesList = result.data.orEmpty()

                        // Atualiza o estado com os novos exercícios
                        state = state.copy(
                            workout = state.workout + exercisesList
                        )

                        // adiciona o treino gerado na cache
                        viewModelScope.launch {
                            workoutLocalRepository.insertWorkout(bodyParts)
                        }
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
                state = state.copy(isGeneratingWorkout = false)
            }
        }
    }

    data class ScreenState(
        val isLoading: Boolean = false,
        val isGeneratingWorkout: Boolean = false,
        val error: String? = null,
        val workout: List<ExerciseItem> = emptyList()
    )
}
