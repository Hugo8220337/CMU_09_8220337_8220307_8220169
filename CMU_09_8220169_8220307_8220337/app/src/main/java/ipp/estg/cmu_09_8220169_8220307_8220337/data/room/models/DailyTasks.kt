package ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "dailyTasks")
data class DailyTasks(
    @PrimaryKey
    val date: String,
    val userId: String = "",
    val gallonOfWater: Boolean = false,
    val twoWorkouts: Boolean = false,
    val followDiet: Boolean = false,
    val readTenPages: Boolean = false,
    val takeProgressPicture: String = "",
) {
    constructor(
        userId: String = "",
        gallonOfWater: Boolean = false,
        twoWorkouts: Boolean = false,
        followDiet: Boolean = false,
        readTenPages: Boolean = false,
        takeProgressPicture: String = ""
    ) : this(
        date = LocalDate.now().toString(),
        userId = userId,
        gallonOfWater = gallonOfWater,
        twoWorkouts = twoWorkouts,
        followDiet = followDiet,
        readTenPages = readTenPages,
        takeProgressPicture = takeProgressPicture
    )
}