package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import android.util.Log
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.UserFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.WorkoutFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.models.exerciceDbApi.ExerciseItemDataResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.models.exerciceDbApi.ExercisesRetrofitResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.apis.ExerciseDbApi
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.WorkoutDao
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Converter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.UUID


class WorkoutRepository(
    private val exerciseDbApi: ExerciseDbApi,
    private val workoutDao: WorkoutDao
) {

    private val workoutFirestoreRepository: WorkoutFirestoreRepository = WorkoutFirestoreRepository()

    suspend fun getExercisesByBodyParts(
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
            //insertWorkoutInCache(bodyParts)
            insertWorkoutInCacheAndFirebase(bodyParts)
        }

        return allExercises
    }

    suspend fun getWorkouts(): List<Workout> {
        syncWorkoutsFromFirebase()
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

    private suspend fun insertWorkoutInCacheAndFirebase(trainedBodyParts: List<String>) {
        try {
            val converter = Converter()
            val exercisedBodyPartsString = converter.fromStringList(trainedBodyParts)

            // Cria o Workout (sem ID definido; será gerado pelo Room)
            val workoutToInsert = Workout(trainedBodyParts = exercisedBodyPartsString)

            // Salva no Room e obtém o ID gerado
            val generatedId = workoutDao.insertWorkout(workoutToInsert)

            // Salva no Firebase com o ID gerado pelo Room
            workoutFirestoreRepository.insertWorkoutInFirebase(generatedId, trainedBodyParts)
        } catch (e: Exception) {
            Log.d("WorkoutRepository", "Erro ao inserir o workout no cache ou Firebase", e)
        }
    }


    private suspend fun syncWorkoutsFromFirebase() {
        try {
            val firebaseWorkouts = workoutFirestoreRepository.getAllWorkoutsFromFirebase()

            // Save each workout from Firebase to Room if it doesn't already exist
            for (firebaseWorkout in firebaseWorkouts) {
                val localWorkout = workoutDao.getWorkoutById(firebaseWorkout.id.toString())
                if (localWorkout == null) {
                    workoutDao.insertWorkout(firebaseWorkout)
                }
            }
        } catch (e: Exception) {
            Log.d("WorkoutRepository", "Error syncing workouts from Firebase", e)
        }
    }

}