package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models

class UserCollection(
    val fieldId: String = "",
    val fieldName: String = "",
    val fieldBirthDate: String = "",
    val fieldWeight: Double = 0.0,
    val fieldHeight: Double = 0.0,
    val fieldIsFirstRun: Boolean = true
) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_BIRTH_DATE = "birthDate"
        const val FIELD_WEIGHT = "weight"
        const val FIELD_HEIGHT = "height"
        const val FIELD_IS_FIRST_RUN = "isFirstRun"
    }
}