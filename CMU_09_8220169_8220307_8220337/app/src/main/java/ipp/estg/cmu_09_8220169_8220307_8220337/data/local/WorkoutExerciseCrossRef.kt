package ipp.estg.cmu_09_8220169_8220307_8220337.data.local

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "workout_exercise_cross_ref",
    primaryKeys = ["workoutId", "exerciseId"],
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE // Se quiser excluir registros vinculados ao deletar um Workout
        ),
        ForeignKey(
            entity = ExerciseItem::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE // Exclui registros vinculados ao deletar um ExerciseItem
        )
    ]
)
data class WorkoutExerciseCrossRef(
    val workoutId: Int,
    val exerciseId: String
)