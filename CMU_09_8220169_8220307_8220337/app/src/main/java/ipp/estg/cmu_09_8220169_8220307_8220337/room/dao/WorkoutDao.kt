package ipp.estg.cmu_09_8220169_8220307_8220337.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Workout


/**
 * suspend functions permitem executar sem bloquear o programa
 */
@Dao
interface WorkoutDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout) : Long


    @Query("SELECT * FROM workout")
    suspend fun getWorkoutsWithExercises(): List<Workout>

}