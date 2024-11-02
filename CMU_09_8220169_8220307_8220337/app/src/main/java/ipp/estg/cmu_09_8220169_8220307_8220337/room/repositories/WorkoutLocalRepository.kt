package ipp.estg.cmu_09_8220169_8220307_8220337.room.repositories

import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.WorkoutDao
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Converter

class WorkoutLocalRepository(
    private val workoutDao: WorkoutDao
) {

    suspend fun insertWorkout(trainedBodyParts: List<String>) {
        try {
            val converter = Converter()
            val exercisedBodyPartsString = converter.fromStringList(trainedBodyParts)

            // Cria o Workout
            val workoutToInsert = Workout(trainedBodyParts = exercisedBodyPartsString)

            workoutDao.insertWorkout(workoutToInsert)
        } catch (e: Exception) {
            throw e
        }

        suspend fun getWorkoutsFromCache(): List<Workout> {
            return workoutDao.getWorkoutsWithExercises()
        }
    }

}