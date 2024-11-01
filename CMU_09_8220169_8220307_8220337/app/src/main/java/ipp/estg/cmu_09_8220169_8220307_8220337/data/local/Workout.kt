package ipp.estg.cmu_09_8220169_8220307_8220337.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout")
class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateOfWorkout: String = LocalDate.now().toString()
)