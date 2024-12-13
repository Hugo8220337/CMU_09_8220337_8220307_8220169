package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val isFirstRun: Boolean = true
)
