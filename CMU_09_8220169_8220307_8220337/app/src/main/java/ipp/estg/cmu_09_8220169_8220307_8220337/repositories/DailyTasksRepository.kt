package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.DailyTasksFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.DailyTasksDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import java.time.LocalDate

class DailyTasksRepository(
    private val dailyTasksDao: DailyTasksDao
) {

    private val dailyTasksFirestoreRepository: DailyTasksFirestoreRepository =
        DailyTasksFirestoreRepository()

    suspend fun insertTasks(
        userId: String,
        gallonOfWater: Boolean,
        twoWorkouts: Boolean,
        followDiet: Boolean,
        readTenPages: Boolean,
        takeProgressPicture: String,
    ) {
        try {
            // Inserir ou atualizar tarefas diárias
            val tasks = DailyTasks(
                userId = userId,
                gallonOfWater = gallonOfWater,
                twoWorkouts = twoWorkouts,
                followDiet = followDiet,
                readTenPages = readTenPages,
                takeProgressPicture = takeProgressPicture
            )

            // Inserir tarefas na base de dados local
            dailyTasksDao.insertTasks(tasks)

            // Inserir tarefas na base de dados remota
            dailyTasksFirestoreRepository.insertDailyTaskInFirebase(tasks)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getTodayTasksLiveData(userId: String): LiveData<DailyTasks> {
        // Sincronizar tarefas diárias do Firebase
        syncDailyTasksFromFirebase()

        // Obter tarefas diárias de hoje
        val currentDate = LocalDate.now().toString()
        return dailyTasksDao.getTasksByDateLiveData(currentDate, userId)
    }

    fun getTodayTasks(): DailyTasks {
        val currentDate = LocalDate.now().toString()

        return dailyTasksDao.getTasksByDate(currentDate)
    }

    fun areTodaysTasksDone(): Boolean {
        val dailyTasks = getTodayTasks()

        if (dailyTasks == null || dailyTasks.followDiet == null || dailyTasks.twoWorkouts == null || dailyTasks.readTenPages == null || dailyTasks.gallonOfWater == null) {
            return false
        }
        val diet = dailyTasks.followDiet
        val workouts = dailyTasks.twoWorkouts
        val tenPages = dailyTasks.readTenPages
        val water = dailyTasks.gallonOfWater

        val condition = (diet && workouts && tenPages && water)

        return condition
    }

    suspend fun getTodaysProgressPicture(): String {
        val currentDate = LocalDate.now().toString()
        val progressPicture = dailyTasksDao.getProgressPathPictureByDate(currentDate)

        if (progressPicture == null) {
            return ""
        }

        return progressPicture
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

    suspend fun getAllTasks(): List<DailyTasks> {
        syncDailyTasksFromFirebase()
        return dailyTasksDao.getAllTasks()
    }

    suspend fun getAllUserTasks(userId: String): List<DailyTasks> {
        syncDailyTasksFromFirebase()
        return dailyTasksDao.getAllUserTasks(userId)
    }

    private suspend fun syncDailyTasksFromFirebase() {
        try {
            val firebaseDailyTasks =
                dailyTasksFirestoreRepository.getAllUserDailyTasksFromFirebase()

            // Save each workout from Firebase to Room if it doesn't already exist
            if (firebaseDailyTasks != null) {
                dailyTasksDao.insertTasks(firebaseDailyTasks)
            }
        } catch (e: Exception) {
            Log.e("DailyTasksRepository", "Error syncing daily tasks from Firebase", e)
        }
    }
}