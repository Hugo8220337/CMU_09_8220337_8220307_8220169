package ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi

data class ExerciseItem(
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
    val id: String,
    val instructions: List<String>,
    val name: String,
    val secondaryMuscles: List<String>,
    val target: String
)