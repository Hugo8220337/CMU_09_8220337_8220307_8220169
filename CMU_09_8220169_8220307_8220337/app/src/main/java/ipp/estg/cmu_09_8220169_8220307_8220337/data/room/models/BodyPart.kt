package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bodyPart")
data class BodyPart(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = ""
)