package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks


/**
 * suspend functions permitem executar sem bloquear o programa
 */
@Dao
interface DailyTasksDao {

    @Query("SELECT * FROM dailyTasks WHERE date = :date AND userId = :userId")
    fun getTasksByDateLiveData(date: String, userId: String): LiveData<DailyTasks>

    @Query("SELECT * FROM dailyTasks WHERE date = :date")
    fun getTasksByDate(date: String): DailyTasks

    @Query("SELECT * FROM dailyTasks ORDER BY date DESC")
    suspend fun getAllTasks(): List<DailyTasks>

    @Query("SELECT * FROM dailyTasks WHERE userId = :userId ORDER BY date DESC")
    suspend fun getAllUserTasks(userId: String): List<DailyTasks>

    @Query("SELECT takeProgressPicture FROM dailyTasks WHERE date = :date")
    suspend fun getProgressPathPictureByDate(date: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: DailyTasks): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<DailyTasks>)

    @Query("DELETE FROM dailyTasks")
    suspend fun deleteAllTasks()

}