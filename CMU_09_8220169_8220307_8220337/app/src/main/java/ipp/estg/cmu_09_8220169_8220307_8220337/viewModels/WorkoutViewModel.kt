package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.Hard75Application
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.ExerciseItem
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Resource
import java.util.Locale

class WorkoutViewModel(
    application: Application
) : AndroidViewModel(application) {

    val exerciseDbApiRepository = Hard75Application.appModule.exerciseDbApiRepository

    var state by mutableStateOf(ScreenState())
        private set

    suspend fun generateWorkout(bodyParts: List<String>) {
        state = state.copy(isGeneratingWorkout = true)

        try {
            for (bodyPart in bodyParts) {
                // TODO tirar estes magic numbers
                val result = exerciseDbApiRepository.getExercisesByBodyPart(
                    bodyPart = bodyPart.lowercase(
                        Locale.ROOT
                    ),
                    limit = 10,
                    offset = 0
                )

                when (result) {
                    is Resource.Success -> {
                        val exercisesList = result.data.orEmpty() // orEmpty() para evitar null

                        state = state.copy(
                            workout = state.workout + exercisesList
                        )
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            error = result.message!!
                        )
                    }
                }
            }
        } catch (e: Exception) {
            state = state.copy(error = "Error generating workout. " + e.localizedMessage)
        }

        state = state.copy(isGeneratingWorkout = false)
    }
}

data class ScreenState(
    val isLoading: Boolean = false,
    val isGeneratingWorkout: Boolean = false,
    val error: String? = null,
    val workout: List<ExerciseItem> = emptyList()
)