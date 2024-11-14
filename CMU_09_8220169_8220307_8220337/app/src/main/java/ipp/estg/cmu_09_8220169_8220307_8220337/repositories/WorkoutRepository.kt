package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

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
            val response = getExercisesByBodyPartFromApi(part.lowercase(), limit, offset)

            if (!response.isSuccessful) {
                throw Exception("Error code: ${response.code()}")
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

    private suspend fun insertWorkoutInCache(trainedBodyParts: List<String>) {
        try {
            val converter = Converter()
            val exercisedBodyPartsString = converter.fromStringList(trainedBodyParts)

            // Cria o Workout
            val workoutToInsert = Workout(trainedBodyParts = exercisedBodyPartsString)

            workoutDao.insertWorkout(workoutToInsert)
        } catch (e: Exception) {
            throw e
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