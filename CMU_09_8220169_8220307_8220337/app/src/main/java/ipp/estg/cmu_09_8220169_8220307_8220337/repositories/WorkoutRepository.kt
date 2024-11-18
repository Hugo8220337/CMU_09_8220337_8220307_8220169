package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import android.util.Log
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.ExerciseItemDataResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.ExercisesRetrofitResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.apis.ExerciseDbApi
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.WorkoutDao
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Converter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface IWorkoutRepository {
    suspend fun getExercisesByBodyParts(
        bodyParts: List<String>,
        limit: Int,
        offset: Int,
    ): List<ExerciseItemDataResponse>

    suspend fun getWorkouts(): List<Workout>
}

class WorkoutRepository(
    private val exerciseDbApi: ExerciseDbApi,
    private val workoutDao: WorkoutDao
) : IWorkoutRepository {

    override suspend fun getExercisesByBodyParts(
        bodyParts: List<String>,
        limit: Int,
        offset: Int,
    ): List<ExerciseItemDataResponse> {

        var allExercises: List<ExerciseItemDataResponse> = emptyList()

        // Get BodyParts from API
        for (part in bodyParts) {
            val response = try {
                getExercisesByBodyPartFromApi(part.lowercase(), limit, offset)
            } catch (e: Exception) {
                null
            }

            if (response == null || !response.isSuccessful) {
                // Log the error and continue with the next body part
                Log.d("WorkoutRepository", "Error fetching exercises for body part $part")
                continue
            }

            val exercises = response.body()
            if (!exercises.isNullOrEmpty()) {
                allExercises = allExercises + exercises
            }
        }

        // Insert trained body parts in cache on a different thread
        withContext(Dispatchers.IO) {
            insertWorkoutInCache(bodyParts)
        }

        return allExercises
    }

    override suspend fun getWorkouts(): List<Workout> {
        return workoutDao.getWorkouts()
    }

    private suspend fun insertWorkoutInCache(trainedBodyParts: List<String>) {
        try {
            val converter = Converter()
            val exercisedBodyPartsString = converter.fromStringList(trainedBodyParts)

            // Cria o Workout
            val workoutToInsert = Workout(trainedBodyParts = exercisedBodyPartsString)

            workoutDao.insertWorkout(workoutToInsert)
        } catch (e: Exception) {
            Log.d("WorkoutRepository", "Error inserting workout in cache")
        }
    }

    private suspend fun getExercisesByBodyPartFromApi(
        bodyPart: String,
        limit: Int,
        offset: Int,
    ): Response<ExercisesRetrofitResponse> {
        return withContext(Dispatchers.IO) {
            val call = exerciseDbApi.getExercisesByBodyPart(
                bodyPart = bodyPart,
                limit = limit,
                offset = offset
            )
            call.execute()
        }
    }

}