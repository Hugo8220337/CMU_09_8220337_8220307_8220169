package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout")
class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trainedBodyParts: String, // isto é uma lista de body parts, vai-se usar o converter para guardar em String
    val dateOfWorkout: String = LocalDate.now().toString()
)