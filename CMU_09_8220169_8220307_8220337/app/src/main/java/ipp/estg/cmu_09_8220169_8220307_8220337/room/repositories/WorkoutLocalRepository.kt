package ipp.estg.cmu_09_8220169_8220307_8220337.room.repositories

import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.ExerciseItem
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.WorkoutExerciseCrossRef
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.Exercises
import ipp.estg.cmu_09_8220169_8220307_8220337.room.Dao
import java.util.Date
import kotlin.math.absoluteValue

class WorkoutLocalRepository(
    private val dao: Dao
) {

    suspend fun insertWorkout(exercises: List<ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.ExerciseItem>) {
        try {
            // Cria o Workout
            val workout = Workout()

            // Insere o Workout na BD e captura o ID gerado
            val workoutId = dao.insertWorkout(workout).toInt()


            val exerciseItems = mutableListOf<ExerciseItem>()
            val crossRefs = mutableListOf<WorkoutExerciseCrossRef>()

            // Preenche as listas de ExerciseItem e WorkoutExerciseCrossRef
            for (exercise in exercises) {
                // Converte cada exercício da API para a versão da cache
                exerciseItems += ExerciseItem(
                    id = exercise.id,
                    name = exercise.name,
                    bodyPart = exercise.bodyPart,
                    equipment = exercise.equipment,
                    gifUrl = exercise.gifUrl,
                    target = exercise.target
                )

                // Cria a relação Workout-Exercise com o ID gerado
                crossRefs += WorkoutExerciseCrossRef(
                    workoutId = workoutId,
                    exerciseId = exercise.id
                )
            }

            // Insere os exercícios e as referências cruzadas na cache
            dao.insertExerciseItems(exerciseItems)
            dao.insertWorkoutExerciseCrossRefs(crossRefs)
        } catch (e: Exception) {
            throw e
        }

        suspend fun getWorkoutsFromCache(): List<Workout> {
            return dao.getWorkoutsWithExercises()
        }
    }

}