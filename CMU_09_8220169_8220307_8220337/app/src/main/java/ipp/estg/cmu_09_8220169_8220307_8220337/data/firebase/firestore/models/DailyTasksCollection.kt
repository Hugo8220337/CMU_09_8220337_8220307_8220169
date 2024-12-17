package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models

class DailyTasksCollection(
    val fieldId: String = "",
    val fieldDate: String = "",
    val fieldUserId : String = "",
    val fieldGallonOfWater: Boolean = false,
    val fieldTwoWorkouts: Boolean = false,
    val fieldFollowDiet: Boolean = false,
    val fieldReadTenPages: Boolean = false,
    val fieldTakeProgressPicture: String = "",
) {
    companion object{
        const val FIELD_ID = "id"
        const val FIELD_DATE = "date"
        const val FIELD_USER_ID = "userId"
        const val FIELD_GALLON_OF_WATER = "gallonOfWater"
        const val FIELD_TWO_WORKOUTS = "twoWorkouts"
        const val FIELD_FOLLOW_DIET = "followDiet"
        const val FIELD_READ_TEN_PAGES = "readTenPages"
        const val FIELD_TAKE_PROGRESS_PICTURE = "takeProgressPicture"
    }
}