package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout


/**
 * suspend functions permitem executar sem bloquear o programa
 */
@Dao
interface WorkoutDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout) : Long


    @Query("SELECT * FROM workout")
    suspend fun getWorkouts(): List<Workout>

    @Query("SELECT * FROM workout WHERE id = :id")
    suspend fun getWorkoutById(id: String): Workout?

    @Query("DELETE FROM workout WHERE id = :id")
    suspend fun deleteWorkoutById(id: String)

}