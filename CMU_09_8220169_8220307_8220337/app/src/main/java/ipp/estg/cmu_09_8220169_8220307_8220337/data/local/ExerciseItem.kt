package ipp.estg.cmu_09_8220169_8220307_8220337.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exerciseItem")
data class ExerciseItem(
    @PrimaryKey
    val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
//    val instructions: List<String>,
//    val secondaryMuscles: List<String>,
    val target: String
)