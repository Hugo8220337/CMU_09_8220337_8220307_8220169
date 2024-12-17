package ipp.estg.cmu_09_8220169_8220307_8220337.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.DailyTasksDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.QuoteDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.RunningDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.UserDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.WorkoutDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.BodyPart
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Quote
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Running
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.LOCAL_DB_NAME

@Database(
    entities = [
        Workout::class,
        BodyPart::class,
        DailyTasks::class,
        Quote::class,
        User::class,
        Running::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val workoutDao: WorkoutDao
    abstract val dailyTaskCompletionDao: DailyTasksDao
    abstract val quotesDao: QuoteDao
    abstract val runningDao: RunningDao

    companion object{
        private var INSTANCE: LocalDatabase?=null

        fun getDatabase(context: Context): LocalDatabase {
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