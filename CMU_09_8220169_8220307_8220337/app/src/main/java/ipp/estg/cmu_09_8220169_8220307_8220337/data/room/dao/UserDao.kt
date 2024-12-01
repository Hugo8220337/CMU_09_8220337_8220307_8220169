package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User) : Long

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUser(id: String): User
}
