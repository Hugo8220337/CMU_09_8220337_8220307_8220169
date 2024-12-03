package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models

import java.time.LocalDate

class WorkoutCollection(
    val fieldId: Long = 0,
    val fieldUserId : String = "",
    val fieldTrainedBodyParts: String = "",
    val fieldDateOfWorkou: String = LocalDate.now().toString()

) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_USER_ID = "userId"
        const val FIELD_TRAINED_BODY_PARTS = "trainedBodyParts"
        const val FIELD_DATE_WORKOUT = "dateOfWorkout"
    }
}