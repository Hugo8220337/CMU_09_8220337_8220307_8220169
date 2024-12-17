package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.WorkoutCollection
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Converter
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class WorkoutFirestoreRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

    private val authFirebaseRepository: AuthFirebaseRepository = AuthFirebaseRepository()

    suspend fun insertWorkoutInFirebase(workoutId: Long, trainedBodyParts: List<String>) {
        try {
            val userId = authFirebaseRepository.getCurrentUser()?.uid
            val converter = Converter()
            val exercisedBodyPartsString = converter.fromStringList(trainedBodyParts)

            val workoutData = mapOf(
                WorkoutCollection.FIELD_ID to workoutId,
                WorkoutCollection.FIELD_USER_ID to userId,
                WorkoutCollection.FIELD_TRAINED_BODY_PARTS to exercisedBodyPartsString,
                WorkoutCollection.FIELD_DATE_WORKOUT to LocalDate.now().toString()
            )

            firestore.collection(CollectionsNames.workoutCollection)
                .document(workoutId.toString()) // Converte o Long para String para usar como ID do documento
                .set(workoutData)
                .await()
        } catch (e: Exception) {
            Log.d("WorkoutFirestoreRepository", "Erro ao inserir workout no Firebase", e)
        }
    }

    // Get trained body parts from Firebase
    suspend fun getWorkoutFromFirebase(): List<String>? {
        return try {
            val result = firestore.collection(CollectionsNames.workoutCollection)
                .document("trainedBodyParts")
                .get()
                .await()

            if (result != null && result.exists()) {
                val document = result.data
                val trainedBodyPartsString =
                    document?.get(WorkoutCollection.FIELD_TRAINED_BODY_PARTS) as String

                val converter = Converter()
                converter.toStringList(trainedBodyPartsString)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Get all workouts from Firebase
    suspend fun getAllWorkoutsFromFirebase(): List<Workout> {
        return try {
            val result = firestore.collection(CollectionsNames.workoutCollection)
                .get()
                .await()

            // Convert the result into a list of Workout objects
            result.documents.mapNotNull { document ->
                val id = document.getLong(WorkoutCollection.FIELD_ID) // Fetch the ID
                val trainedBodyPartsString =
                    document.getString(WorkoutCollection.FIELD_TRAINED_BODY_PARTS) // Fetch the body parts

                if (id != null && trainedBodyPartsString != null) {
                    Workout(
                        id = id,
                        userId = document.getString(WorkoutCollection.FIELD_USER_ID) ?: "",
                        trainedBodyParts = trainedBodyPartsString // Keep the raw string in the model
                    )
                } else {
                    null // Skip documents with missing or invalid data
                }
            }
        } catch (e: Exception) {
            Log.d("WorkoutFirestoreRepository", "Error fetching workouts from Firebase", e)
            emptyList() // Return an empty list in case of errors
        }
    }

    suspend fun getAllUserWorkoutsFromFirebase(): List<Workout> {
        val userId = authFirebaseRepository.getCurrentUser()?.uid
        return try {
            val result = firestore.collection(CollectionsNames.workoutCollection)
                .whereEqualTo(WorkoutCollection.FIELD_USER_ID, userId)
                .get()
                .await()

            // Convert the result into a list of Workout objects
            result.documents.mapNotNull { document ->
                val id = document.getLong(WorkoutCollection.FIELD_ID) // Fetch the ID
                val trainedBodyPartsString =
                    document.getString(WorkoutCollection.FIELD_TRAINED_BODY_PARTS) // Fetch the body parts

                if (id != null && trainedBodyPartsString != null) {
                    Workout(
                        id = id,
                        userId = document.getString(WorkoutCollection.FIELD_USER_ID) ?: "",
                        trainedBodyParts = trainedBodyPartsString // Keep the raw string in the model
                    )
                } else {
                    null // Skip documents with missing or invalid data
                }
            }
        } catch (e: Exception) {
            Log.d("WorkoutFirestoreRepository", "Error fetching workouts from Firebase", e)
            emptyList() // Return an empty list in case of errors
        }
    }
}