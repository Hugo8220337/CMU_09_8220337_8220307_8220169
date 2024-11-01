package ipp.estg.cmu_09_8220169_8220307_8220337.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.ExerciseItem
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.WorkoutExerciseCrossRef


/**
 * suspend functions permitem executar sem bloquear o programa
 */
@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseItem(exercise: ExerciseItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseItems(exercises: List<ExerciseItem>) : List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExerciseCrossRefs(crossRefs: List<WorkoutExerciseCrossRef>) : List<Long>

    @Query("SELECT * FROM workout")
    suspend fun getWorkoutsWithExercises(): List<Workout>

}