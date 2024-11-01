package ipp.estg.cmu_09_8220169_8220307_8220337.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.BodyPart
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.ExerciseItem
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.WorkoutExerciseCrossRef

@Database(
    entities = [
        Workout::class,
        BodyPart::class,
        ExerciseItem::class,
        WorkoutExerciseCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract val dao: Dao
}