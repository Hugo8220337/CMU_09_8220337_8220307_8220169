package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.DailyTasksFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.DailyTasksDao
import java.time.LocalDate

class DailyTasksRepository(
    private val dailyTasksDao: DailyTasksDao
){

    private val dailyTasksFirestoreRepository: DailyTasksFirestoreRepository = DailyTasksFirestoreRepository()

    suspend fun insertTasks(
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

            // Inserir tarefas na base de dados local
            dailyTasksDao.insertTasks(tasks)
            // Inserir tarefas na base de dados remota
            dailyTasksFirestoreRepository.insertDailyTaskInFirebase(tasks)

        } catch (e: Exception) {
            throw e
        }
    }

      fun getTodayTasks(): LiveData<DailyTasks> {
         val currentDate = LocalDate.now().toString()
          //syncDailyTasksFromFirebase()
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

    suspend fun getTodaysProgressPicture(): String {
        val currentDate = LocalDate.now().toString()
        val progressPicture = dailyTasksDao.getProgressPathPictureByDate(currentDate)

        if(progressPicture == null) {
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
            if (task.gallonOfWater && task.twoWorkouts && task.followDiet && task.readTenPages   ) {
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

    private suspend fun syncDailyTasksFromFirebase(){
        try{
            val firebaseDailyTasks = dailyTasksFirestoreRepository.getAllDailyTasksFromFirebase()
            // Save each workout from Firebase to Room if it doesn't already exist
            if (firebaseDailyTasks != null) {
                for (firebaseDailyTask in firebaseDailyTasks) {
                    val localDailyTask = dailyTasksDao.getTasksByDate(firebaseDailyTask.date)
                    if (localDailyTask == null) {
                        dailyTasksDao.insertTasks(firebaseDailyTask)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("DailyTasksRepository", "Error syncing daily tasks from Firebase", e)
        }
    }
}