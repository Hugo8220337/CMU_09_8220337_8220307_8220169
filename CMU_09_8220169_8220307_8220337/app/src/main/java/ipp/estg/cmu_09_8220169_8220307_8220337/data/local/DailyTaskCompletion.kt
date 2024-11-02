package ipp.estg.cmu_09_8220169_8220307_8220337.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dailyTaskCompletion")
data class DailyTaskCompletion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String
)
