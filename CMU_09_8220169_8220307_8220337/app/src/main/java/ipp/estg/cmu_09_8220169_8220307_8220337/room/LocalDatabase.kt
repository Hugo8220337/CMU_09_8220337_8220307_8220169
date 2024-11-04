package ipp.estg.cmu_09_8220169_8220307_8220337.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.BodyPart
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.DailyTaskCompletion
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.DailyTaskCompletionDao
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.WorkoutDao

@Database(
    entities = [
        Workout::class,
        BodyPart::class,
        DailyTaskCompletion::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract val workoutDao: WorkoutDao
    abstract val dailyTaskCompletionDao: DailyTaskCompletionDao
}