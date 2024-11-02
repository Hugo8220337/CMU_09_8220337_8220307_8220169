package ipp.estg.cmu_09_8220169_8220307_8220337.room.repositories

import android.content.SharedPreferences
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.DailyTaskCompletion
import ipp.estg.cmu_09_8220169_8220307_8220337.preferences.DailyTasksRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.DailyTaskCompletionDao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyTasksLocalRepository(
    private val dailyTasksDao: DailyTaskCompletionDao,
    private val dailyTasksPreferencesRepository: DailyTasksRepository
) {
    // Função para registrar a conclusão das tarefas diárias
    suspend fun recordDailyCompletion() {
        try {
            // Atualizar cache em Room
            val currentDate = getCurrentDate()

            val completion = DailyTaskCompletion(date = currentDate)
            dailyTasksDao.insertCompletion(completion)
        } catch (e: Exception) {
            throw e
        }

    }

//    suspend fun getStreak(): Int {
//        val lastCompletion = dailyTasksDao.getLastCompletion()
//        // Lógica para calcular streak baseada nas datas dos registros no Room
//    }

    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Date())
    }
}