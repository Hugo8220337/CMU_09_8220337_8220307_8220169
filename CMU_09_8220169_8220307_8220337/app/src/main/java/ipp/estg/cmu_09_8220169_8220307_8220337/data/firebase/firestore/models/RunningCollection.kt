package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models

class RunningCollection(
    val fieldId: String = "",
    val fieldUserId : String = "",
    val fieldDistance: Double = 0.0,
    val fieldDuration: String = "",
    val fieldSteps: Int = 0,
    val fieldCalories: Int = 0,
    val fieldDate: String = "",
) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_USER_ID = "userId"
        const val FIELD_DISTANCE = "distance"
        const val FIELD_DURATION = "duration"
        const val FIELD_STEPS = "steps"
        const val FIELD_CALORIES = "calories"
        const val FIELD_DATE = "date"
    }
}