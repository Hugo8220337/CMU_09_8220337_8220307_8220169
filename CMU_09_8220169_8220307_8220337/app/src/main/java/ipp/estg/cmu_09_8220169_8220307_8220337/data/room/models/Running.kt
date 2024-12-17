package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "running")
data class Running(
    @PrimaryKey
    val id: String,
    val userId: String = "",
    val distance: Double,
    val duration: String,
    val steps: Int,
    val calories: Int,
    val date: String
) {
    constructor(
        id: String = "",
        userId: String = "",
        distance: Double,
        duration: String = "",
        steps: Int,
        calories: Int
    ) : this(
        id = id,
        distance = distance,
        duration = duration,
        steps = steps,
        calories = calories,
        date = LocalDate.now().toString()
    )
}