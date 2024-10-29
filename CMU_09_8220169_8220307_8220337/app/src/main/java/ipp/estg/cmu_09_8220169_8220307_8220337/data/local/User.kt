package ipp.estg.cmu_09_8220169_8220307_8220337.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


// TODO apagar porque isto é temporário
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val authToken: String = ""
)
