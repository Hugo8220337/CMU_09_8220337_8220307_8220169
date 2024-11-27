package ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.models.exerciceDbApi

data class ExerciseItemDataResponse(
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
    val id: String,
    val instructions: List<String>,
    val name: String,
    val secondaryMuscles: List<String>,
    val target: String
)