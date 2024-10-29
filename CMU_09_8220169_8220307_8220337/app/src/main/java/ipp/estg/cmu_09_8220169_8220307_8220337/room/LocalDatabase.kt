package ipp.estg.cmu_09_8220169_8220307_8220337.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.User

@Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract val dao: Dao
}