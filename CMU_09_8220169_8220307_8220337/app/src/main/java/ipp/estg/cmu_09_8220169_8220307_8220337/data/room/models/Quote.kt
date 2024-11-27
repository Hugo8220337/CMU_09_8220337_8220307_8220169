package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "quote")
class Quote (
    @PrimaryKey
    val date: String = LocalDate.now().toString(),
    val quote: String,
)