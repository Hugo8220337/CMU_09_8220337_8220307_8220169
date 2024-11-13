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

    fun areTodaysTasksDone(): Boolean {
        val dailyTasks = getTodayTasks()

        val diet = dailyTasks.value?.followDiet
        val workouts = dailyTasks.value?.twoWorkouts
        val tenPages = dailyTasks.value?.readTenPages
        val water = dailyTasks.value?.gallonOfWater

        val condition = (diet == true && workouts == true && tenPages == true && water == true)

        return condition
    }

    suspend fun getStreak(): Int {
        val tasks = dailyTasksDao.getAllTasks()  // Busca todas as tarefas
        var streak = 0
        var previousDate: LocalDate? = null

        for (task in tasks) {
            val taskDate = LocalDate.parse(task.date)

            // Verifica se todas as tarefas foram concluídas nesse dia
            if (task.gallonOfWater && task.twoWorkouts && task.followDiet && task.readTenPages) {
                if (previousDate == null || taskDate == previousDate.minusDays(1)) {
                    streak++
                    previousDate = taskDate
                } else {
                    break
                }
            } else {
                break
            }
        }

        return streak
    }
}