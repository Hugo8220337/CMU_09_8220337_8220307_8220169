package ipp.estg.cmu_09_8220169_8220307_8220337.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.DailyTasks


/**
 * suspend functions permitem executar sem bloquear o programa
 */
@Dao
interface DailyTasksDao {

    @Query("SELECT * FROM dailyTasks WHERE date = :date")
    fun getTasksByDate(date: String): LiveData<DailyTasks>

    @Query("SELECT * FROM DailyTasks ORDER BY date DESC")
    suspend fun getAllTasks(): List<DailyTasks>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: DailyTasks): Long

}