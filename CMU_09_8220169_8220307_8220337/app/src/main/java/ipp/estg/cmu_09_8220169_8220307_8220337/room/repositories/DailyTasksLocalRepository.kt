package ipp.estg.cmu_09_8220169_8220307_8220337.room.repositories

import androidx.lifecycle.LiveData
import androidx.room.util.convertByteToUUID
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.DailyTasksDao
import java.time.LocalDate

class DailyTasksLocalRepository(
    private val dailyTasksDao: DailyTasksDao,
) {
    // Função para registrar a conclusão das tarefas diárias
    /*suspend fun recordDailyCompletion() {
        try {
            // Atualizar cache em Room
            val currentDate = getCurrentDate()

            val completion = DailyTasks(date = currentDate)
            dailyTasksDao.insertCompletion(completion)
        } catch (e: Exception) {
            throw e
        }

    }*/

    suspend fun insertTasks(
        gallonOfWater: Boolean,
        twoWorkouts: Boolean,
        followDiet: Boolean,
        readTenPages: Boolean,
        takeProgressPicture: String = "",
    ) {
        try {
            // Inserir ou atualizar tarefas diárias
            val tasks = DailyTasks(
                gallonOfWater = gallonOfWater,
                twoWorkouts = twoWorkouts,
                followDiet = followDiet,
                readTenPages = readTenPages,
                takeProgressPicture = takeProgressPicture
            )

            dailyTasksDao.insertTasks(tasks)
        } catch (e: Exception) {
            throw e
        }
    }

    fun getTodayTasks(): LiveData<DailyTasks> {
        val currentDate = LocalDate.now().toString()

        return dailyTasksDao.getTasksByDate(currentDate)
    }
}