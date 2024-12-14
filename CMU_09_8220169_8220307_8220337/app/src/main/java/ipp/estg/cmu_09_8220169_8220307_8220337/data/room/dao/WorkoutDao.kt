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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<Workout>)

    @Query("SELECT * FROM workout WHERE userId = :userId")
    suspend fun getWorkouts(userId: String): List<Workout>

    @Query("SELECT * FROM workout WHERE id = :id")
    suspend fun getWorkoutById(id: String): Workout?

    @Query("SELECT * FROM workout")
    suspend fun getAllWorkouts(): List<Workout>

    @Query("DELETE FROM workout WHERE id = :id")
    suspend fun deleteWorkoutById(id: String)

    @Query("DELETE FROM workout")
    suspend fun deleteAllWorkouts()

}