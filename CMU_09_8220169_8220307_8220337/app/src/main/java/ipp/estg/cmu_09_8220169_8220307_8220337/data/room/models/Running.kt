package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "running")
class Running (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val distance: Double,
    val duration: String,
    val steps: Int,
    val calories: Double,
    val date: String = LocalDate.now().toString()
)
