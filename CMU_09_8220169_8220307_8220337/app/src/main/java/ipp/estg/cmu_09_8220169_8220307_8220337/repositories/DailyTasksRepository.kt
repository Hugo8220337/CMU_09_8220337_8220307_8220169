package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import androidx.lifecycle.LiveData
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.DailyTasksDao
import java.time.LocalDate

interface IDailyTasksRepository {
    suspend fun insertTasks(
        gallonOfWater: Boolean,
        twoWorkouts: Boolean,
        followDiet: Boolean,
        readTenPages: Boolean,
        takeProgressPicture: String = "",
    )

    fun getTodayTasks(): LiveData<DailyTasks>
    fun areTodaysTasksDone(): Boolean
    suspend fun getTodaysProgressPicture(): String
    suspend fun getStreak(): Int
}
class DailyTasksRepository(
    private val dailyTasksDao: DailyTasksDao,
) : IDailyTasksRepository{
    override suspend fun insertTasks(
        gallonOfWater: Boolean,
        twoWorkouts: Boolean,
        followDiet: Boolean,
        readTenPages: Boolean,
        takeProgressPicture: String,
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

    override fun getTodayTasks(): LiveData<DailyTasks> {
        val currentDate = LocalDate.now().toString()

        return dailyTasksDao.getTasksByDate(currentDate)
    }

    override fun areTodaysTasksDone(): Boolean {
        val dailyTasks = getTodayTasks()

        val diet = dailyTasks.value?.followDiet
        val workouts = dailyTasks.value?.twoWorkouts
        val tenPages = dailyTasks.value?.readTenPages
        val water = dailyTasks.value?.gallonOfWater

        val condition = (diet == true && workouts == true && tenPages == true && water == true)

        return condition
    }

    override suspend fun getTodaysProgressPicture(): String {
        val currentDate = LocalDate.now().toString()
        val progressPicture = dailyTasksDao.getProgressPathPictureByDate(currentDate)

        return progressPicture
    }

    override suspend fun getStreak(): Int {
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