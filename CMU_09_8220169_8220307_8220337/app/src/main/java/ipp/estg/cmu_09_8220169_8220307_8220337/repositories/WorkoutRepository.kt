package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import android.util.Log
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.WorkoutFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.apis.ExerciseDbApi
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.models.exerciceDbApi.ExerciseItemDataResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.models.exerciceDbApi.ExercisesRetrofitResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.WorkoutDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Converter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


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
            val generatedId = insertWorkoutInCache(bodyParts)
            insertWorkoutInFirebase(bodyParts, generatedId)
        }

        return allExercises
    }

    suspend fun getAllWorkouts(): List<Workout> {
        syncWorkoutsFromFirebase()
        return workoutDao.getAllWorkouts()
    }

    private suspend fun insertWorkoutInCache(trainedBodyParts: List<String>): Long {
        try {
            val converter = Converter()
            val exercisedBodyPartsString = converter.fromStringList(trainedBodyParts)

            // Cria o Workout
            val workoutToInsert = Workout(trainedBodyParts = exercisedBodyPartsString)

            val generatedId = workoutDao.insertWorkout(workoutToInsert)

            return generatedId

        } catch (e: Exception) {
            Log.d("WorkoutRepository", "Error inserting workout in cache")
            return -1
        }
    }

    private suspend fun insertWorkoutInFirebase(trainedBodyParts: List<String>, workoutId: Long) {
        try {
            workoutFirestoreRepository.insertWorkoutInFirebase(workoutId, trainedBodyParts)
        } catch (e: Exception) {
            Log.d("WorkoutRepository", "Error inserting workout in Firebase")
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
            val firebaseWorkouts = workoutFirestoreRepository.getAllUserWorkoutsFromFirebase()

            if (firebaseWorkouts.isEmpty()) {
                return
            }

            workoutDao.insertWorkouts(firebaseWorkouts)
        } catch (e: Exception) {
            Log.d("WorkoutRepository", "Error syncing workouts from Firebase", e)
        }
    }

    suspend fun getWorkoutsByUserID(userId: String): List<Workout> {
        try {
            syncWorkoutsFromFirebase()
            val firebaseWorkouts = workoutDao.getWorkouts(userId)

            return firebaseWorkouts
        } catch (e: Exception) {
            Log.d("WorkoutRepository", "Error syncing workouts from Firebase", e)
            return emptyList()
        }
    }

}