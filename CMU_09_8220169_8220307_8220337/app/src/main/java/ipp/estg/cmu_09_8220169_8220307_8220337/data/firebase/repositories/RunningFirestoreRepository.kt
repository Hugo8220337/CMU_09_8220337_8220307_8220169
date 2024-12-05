package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.RunningCollection
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.WorkoutCollection
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Running
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class RunningFirestoreRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

    private val authFirebaseRepository: AuthFirebaseRepository = AuthFirebaseRepository()

    // Insert running data in Firebase
    suspend fun insertRunningInFirebase(running: Running) {
        try {
            val userId = authFirebaseRepository.getCurrentUser()?.uid

            // Generate a unique ID combining userId, timestamp, and random number
            val randomNumber = generateUniqueId(userId!!)
            // userId + date +  random number
            val documentId = userId.plus(randomNumber)

            val runningData = mapOf(
                RunningCollection.FIELD_ID to documentId,
                RunningCollection.FIELD_USER_ID to userId,
                RunningCollection.FIELD_DISTANCE to running.distance + 1.0,
                RunningCollection.FIELD_DURATION to running.duration,
                RunningCollection.FIELD_STEPS to running.steps + 1,
                RunningCollection.FIELD_CALORIES to running.calories + 1.0,
                RunningCollection.FIELD_DATE to LocalDate.now().toString()
            )

            firestore.collection(CollectionsNames.runningCollection)
                .document(documentId)
                .set(runningData)
                .await()

        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error inserting running in Firebase", e)
        }
    }

    // Get running data from Firebase by user ID
    suspend fun getRunningFromFirebaseByUserId(): List<Running> {
        return try {
            val userId = authFirebaseRepository.getCurrentUser()?.uid
            Log.d("Firestore", "Fetching data for userId: $userId")

            val result = firestore.collection(CollectionsNames.runningCollection)
                .whereEqualTo(RunningCollection.FIELD_USER_ID, userId)
                .get()
                .await()

            Log.d("Firestore", "Documents: ${result.documents}")

            if (result != null && !result.isEmpty) {
                result.documents.mapNotNull { document ->
                    try {
                        Running(
                            id = document[RunningCollection.FIELD_ID] as String, // Atualizado para String
                            distance = (document[RunningCollection.FIELD_DISTANCE] as Number).toDouble(),
                            duration = document[RunningCollection.FIELD_DURATION] as String,
                            steps = (document[RunningCollection.FIELD_STEPS] as Number).toInt(),
                            calories = (document[RunningCollection.FIELD_CALORIES
                            ] as Number).toDouble(),
                            date = document[RunningCollection.FIELD_DATE] as String
                        )
                    } catch (e: Exception) {
                        Log.d("Firestore", "Error parsing running data", e)
                        null

                    }
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting running from Firebase", e)
            emptyList()
        }
}
    //get all running data from Firebase
    suspend fun getAllRunningFromFirebase(): List<Running> {
        return try{
            val result = firestore.collection(CollectionsNames.runningCollection)
                .get()
                .await()

            result.documents.mapNotNull { document ->
                Running(
                    id = document[RunningCollection.FIELD_ID] as String,
                    distance = document[RunningCollection.FIELD_DISTANCE] as Double,
                    duration = document[RunningCollection.FIELD_DURATION] as String,
                    steps = document[RunningCollection.FIELD_STEPS] as Int,
                    calories = document[RunningCollection.FIELD_CALORIES] as Double,
                    date = document[RunningCollection.FIELD_DATE] as String
                )
            }
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting all running from Firebase", e)
            emptyList()
        }
    }


    // Delete running data from Firebase by ID
    suspend fun deleteRunningFromFirebaseById(runningId: Long) {
        try {
            firestore.collection(CollectionsNames.runningCollection)
                .document(runningId.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error deleting running from Firebase", e)
        }
    }

    // Generate a unique ID combining userId, timestamp, and random number
    private fun generateUniqueId(userId: String): String {
        val timestamp = System.currentTimeMillis()
        val random = (0..999999).random() // Random number between 0 and 999999
        return "${userId}_${timestamp}_${random}"
    }

}
