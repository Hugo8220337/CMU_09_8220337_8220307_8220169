package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.RunningCollection
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
            val documentId = userId?.let { randomId(it) } // Compound key: runningId + userId

            val runningData = mapOf(
                RunningCollection.FIELD_ID to documentId,
                RunningCollection.FIELD_USER_ID to userId,
                RunningCollection.FIELD_DISTANCE to running.distance,
                RunningCollection.FIELD_DURATION to running.duration,
                RunningCollection.FIELD_STEPS to running.steps,
                RunningCollection.FIELD_CALORIES to running.calories,
                RunningCollection.FIELD_DATE to LocalDate.now().toString()
            )

            if (documentId != null) {
                firestore.collection(CollectionsNames.runningCollection)
                    .document(documentId) // Convert Long to String to use as document ID
                    .set(runningData)
                    .await()
            }
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error inserting running in Firebase", e)
        }
    }

    // Get running data from Firebase by user ID
    suspend fun getRunningFromFirebaseByUserId(): List<Running> {
        val userId = authFirebaseRepository.getCurrentUser()?.uid

        val runningList = mutableListOf<Running>()

        try {
            val querySnapshot = firestore.collection(CollectionsNames.runningCollection)
                .whereEqualTo(RunningCollection.FIELD_USER_ID, userId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val running = Running(
                    id = document[RunningCollection.FIELD_ID] as Long,
                    distance = document[RunningCollection.FIELD_DISTANCE] as Double,
                    duration = document[RunningCollection.FIELD_DURATION] as String,
                    steps = document[RunningCollection.FIELD_STEPS] as Int,
                    calories = document[RunningCollection.FIELD_CALORIES] as Double,
                    date = document[RunningCollection.FIELD_DATE] as String
                )

                runningList.add(running)
            }
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting running from Firebase", e)
        }

        return runningList
    }

    //get all running data from Firebase
    suspend fun getAllRunningFromFirebase(): List<Running> {
        val runningList = mutableListOf<Running>()

        try {
            val querySnapshot = firestore.collection(CollectionsNames.runningCollection)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val running = Running(
                    id = document[RunningCollection.FIELD_ID] as Long,
                    distance = document[RunningCollection.FIELD_DISTANCE] as Double,
                    duration = document[RunningCollection.FIELD_DURATION] as String,
                    steps = document[RunningCollection.FIELD_STEPS] as Int,
                    calories = document[RunningCollection.FIELD_CALORIES] as Double,
                    date = document[RunningCollection.FIELD_DATE] as String
                )

                runningList.add(running)
            }
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting all running from Firebase", e)
        }

        return runningList
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

    private fun randomId(userId : String): String {
        val random = (0..1000).random()
        return "${userId}${random}"
    }

}
