package ipp.estg.cmu_09_8220169_8220307_8220337.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "dailyTasks")
data class DailyTasks(
    @PrimaryKey
    val date: String = LocalDate.now().toString(),

    val gallonOfWater: Boolean = false,
    val twoWorkouts: Boolean = false,
    val followDiet: Boolean = false,
    val readTenPages: Boolean = false,
    val takeProgressPicture: String = "",
)
