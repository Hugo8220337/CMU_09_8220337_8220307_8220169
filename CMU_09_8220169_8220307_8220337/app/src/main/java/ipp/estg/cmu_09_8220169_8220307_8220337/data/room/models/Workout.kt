package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val trainedBodyParts: String,
    val dateOfWorkout: String
) {
    constructor(
        id: Long = 0,
        trainedBodyParts: String
    ) : this(
        id = id,
        trainedBodyParts = trainedBodyParts,
        dateOfWorkout = LocalDate.now().toString()
    )
}