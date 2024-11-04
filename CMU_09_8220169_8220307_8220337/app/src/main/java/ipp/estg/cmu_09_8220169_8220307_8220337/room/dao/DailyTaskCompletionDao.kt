package ipp.estg.cmu_09_8220169_8220307_8220337.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.DailyTaskCompletion


/**
 * suspend functions permitem executar sem bloquear o programa
 */
@Dao
interface DailyTaskCompletionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: DailyTaskCompletion): Long

    @Query("SELECT * FROM dailyTaskCompletion WHERE date = :date")
    suspend fun getCompletionByDate(date: String): DailyTaskCompletion?

    @Query("SELECT * FROM dailyTaskCompletion ORDER BY date DESC LIMIT 1")
    suspend fun getLastCompletion(): DailyTaskCompletion?
}