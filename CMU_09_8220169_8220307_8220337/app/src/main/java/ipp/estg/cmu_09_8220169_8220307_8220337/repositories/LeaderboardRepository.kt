package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.RunningFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.UserFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntryCalories
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntryExerciseTime
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntrySteps


class LeaderboardRepository {

    private val userFirestoreRepository: UserFirestoreRepository = UserFirestoreRepository()
    private val runningFirestoreRepository: RunningFirestoreRepository = RunningFirestoreRepository()


    // Método para obter o leaderboard por calorias
    suspend fun getLeaderboardByCalories(): List<LeaderboardEntryCalories> {
        // Obter todos os usuários (com id e nome)
        val users = userFirestoreRepository.getAllUsers()

        // Lista para armazenar os resultados do leaderboard
        val leaderboard = mutableListOf<LeaderboardEntryCalories>()

        // Para cada usuário, buscamos as calorias mais altas queimadas
        for (user in users) {
            // Chama o método que retorna as calorias mais altas para o usuário específico
            val calories = runningFirestoreRepository.getHighestCaloriesBurnedByUser(user.id)

            // Adiciona a entrada no leaderboard
            leaderboard.add(LeaderboardEntryCalories(user.name, calories))
        }

        // Ordena o leaderboard de forma decrescente pelas calorias queimadas
        return leaderboard.sortedByDescending { it.calories }
    }

    // Método para obter o leaderboard por tempo de exercício
    suspend fun getLeaderboardByExerciseTime(): List<LeaderboardEntryExerciseTime> {
        val users = userFirestoreRepository.getAllUsers()
        val leaderboard = mutableListOf<LeaderboardEntryExerciseTime>()

        for (user in users) {
            val totalTime = runningFirestoreRepository.getTotalExerciseTimeByUser(user.id)
            leaderboard.add(LeaderboardEntryExerciseTime(user.name, totalTime))
        }

        return leaderboard.sortedByDescending { it.exerciseTime } // ou sortedByDescending { it.time }
    }

    // Metodo para obter o leaderboard por passos
    suspend fun getLeaderboardBySteps(): List<LeaderboardEntrySteps> {
        val users = userFirestoreRepository.getAllUsers()
        val leaderboard = mutableListOf<LeaderboardEntrySteps>()

        for (user in users) {
            val steps = runningFirestoreRepository.getHighestStepsByUser(user.id)
            leaderboard.add(LeaderboardEntrySteps(user.name, steps))
        }

        return leaderboard.sortedByDescending { it.steps }
    }


}

