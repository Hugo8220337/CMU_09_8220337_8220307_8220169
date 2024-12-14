package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Running

@Dao
interface RunningDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunning(running: Running) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunnings(runnings: List<Running>)

    @Query("SELECT * FROM running")
    suspend fun getRunnings(): List<Running>

    @Query("SELECT * FROM running ORDER BY date DESC, distance DESC LIMIT 1")
    suspend fun getLastRun(): Running

    @Query("SELECT * FROM running WHERE id = :id")
    suspend fun getRunningById(id: String): Running?

    @Query("DELETE FROM running WHERE id = :id")
    suspend fun deleteRunningById(id: String)

    @Query("DELETE FROM running")
    suspend fun deleteAllRunnings()

}