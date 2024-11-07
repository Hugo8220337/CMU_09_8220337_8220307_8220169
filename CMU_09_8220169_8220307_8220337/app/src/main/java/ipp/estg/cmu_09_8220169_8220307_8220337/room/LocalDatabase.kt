package ipp.estg.cmu_09_8220169_8220307_8220337.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.BodyPart
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.DailyTasksDao
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.WorkoutDao
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.LOCAL_DB_NAME

@Database(
    entities = [
        Workout::class,
        BodyPart::class,
        DailyTasks::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract val workoutDao: WorkoutDao
    abstract val dailyTaskCompletionDao: DailyTasksDao

    companion object{
        private var INSTANCE:LocalDatabase?=null

        fun getDatabase(context: Context):LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    LocalDatabase::class.java,
                    LOCAL_DB_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}