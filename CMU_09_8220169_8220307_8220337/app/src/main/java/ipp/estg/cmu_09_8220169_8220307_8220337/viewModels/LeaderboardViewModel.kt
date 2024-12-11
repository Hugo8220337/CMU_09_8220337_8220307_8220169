package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntryCalories
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntryExerciseTime
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntrySteps
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.LeaderboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel (
    application: Application
) : AndroidViewModel(application) {

    private val leaderboardRepository = LeaderboardRepository()

    // Estado do leaderboard (calorias)
    private val _leaderboardState = MutableStateFlow<List<LeaderboardEntryCalories>>(emptyList())
    val leaderboardState: StateFlow<List<LeaderboardEntryCalories>> = _leaderboardState

    // Estado do leaderboard (tempo de exercício)
    private val _leaderboardStateExerciseTime = MutableStateFlow<List<LeaderboardEntryExerciseTime>>(emptyList())
    val leaderboardStateExerciseTime: StateFlow<List<LeaderboardEntryExerciseTime>> = _leaderboardStateExerciseTime

    // Estado do leaderboard (steps)
    private val _leaderboardStateSteps = MutableStateFlow<List<LeaderboardEntrySteps>>(emptyList())
    val leaderboardStateSteps: StateFlow<List<LeaderboardEntrySteps>> = _leaderboardStateSteps

    // Estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Estado de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()


    // Método para obter o leaderboard por calorias
    suspend fun getLeaderboardByCalories() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val leaderboard = leaderboardRepository.getLeaderboardByCalories()

                if (leaderboard.isNotEmpty()) {
                    _leaderboardState.value = leaderboard
                } else {
                    _errorMessage.value = "Nenhum dado encontrado"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Método para obter o leaderboard por tempo de exercício (Somando todos os tempos de exercício do utilizador)
    suspend fun getLeaderboardByExerciseTime(){
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val leaderboard = leaderboardRepository.getLeaderboardByExerciseTime()

                if (leaderboard.isNotEmpty()) {
                    _leaderboardStateExerciseTime.value = leaderboard
                } else {
                    _errorMessage.value = "Nenhum dado encontrado"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Método para obter o leaderboard por passos (Maior numero de passos dados pelo utilizador)
    suspend fun getLeaderboardBySteps(){
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val leaderboard = leaderboardRepository.getLeaderboardBySteps()

                if (leaderboard.isNotEmpty()) {
                    _leaderboardStateSteps.value = leaderboard
                } else {
                    _errorMessage.value = "Nenhum dado encontrado"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}