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

            // Generate a unique ID combining userId, timestamp, and random number
            val randomNumber = generateUniqueId(userId!!)
            // userId + date +  random number
            val documentId = userId.plus(randomNumber)

            val runningData = mapOf(
                RunningCollection.FIELD_ID to documentId,
                RunningCollection.FIELD_USER_ID to userId,
                RunningCollection.FIELD_DISTANCE to running.distance,
                RunningCollection.FIELD_DURATION to running.duration,
                RunningCollection.FIELD_STEPS to running.steps,
                RunningCollection.FIELD_CALORIES to running.calories,
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

    //get all running data from Firebase
    suspend fun getAllRunningsFromFirebase(): List<Running> {
        return try {
            val result = firestore.collection(CollectionsNames.runningCollection)
                .get()
                .await()

            if(result.isEmpty) {
                Log.d("RunningFirestoreRepository", "No documents found in the collection")
                emptyList()
            } else {
                result.documents.mapNotNull { document ->
                    Running(
                        id = document.getString(RunningCollection.FIELD_ID) ?: "",
                        userId = document.getString(RunningCollection.FIELD_USER_ID) ?: "",
                        distance = (document.getDouble(RunningCollection.FIELD_DISTANCE) ?: 0.0),
                        duration = document.getString(RunningCollection.FIELD_DURATION) ?: "",
                        steps = (document.getLong(RunningCollection.FIELD_STEPS)?.toInt() ?: 0),
                        calories = (document.getLong(RunningCollection.FIELD_CALORIES)?.toInt() ?: 0),
                        date = document.getString(RunningCollection.FIELD_DATE) ?: ""
                    )
                }
            }

        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting all running from Firebase", e)
            emptyList()
        }
    }

    suspend fun getAllUserRunningFromFirebase(): List<Running> {
        val userId = authFirebaseRepository.getCurrentUser()?.uid
        return try {
            val result = firestore.collection(CollectionsNames.runningCollection)
                .whereEqualTo(RunningCollection.FIELD_USER_ID, userId)
                .get()
                .await()

            if(result.isEmpty) {
                Log.d("RunningFirestoreRepository", "No documents found in the collection")
                emptyList()
            } else {
                result.documents.mapNotNull { document ->
                    Running(
                        id = document.getString(RunningCollection.FIELD_ID) ?: "",
                        userId = document.getString(RunningCollection.FIELD_USER_ID) ?: "",
                        distance = (document.getDouble(RunningCollection.FIELD_DISTANCE) ?: 0.0),
                        duration = document.getString(RunningCollection.FIELD_DURATION) ?: "",
                        steps = (document.getLong(RunningCollection.FIELD_STEPS)?.toInt() ?: 0),
                        calories = (document.getLong(RunningCollection.FIELD_CALORIES)?.toInt() ?: 0),
                        date = document.getString(RunningCollection.FIELD_DATE) ?: ""
                    )
                }
            }

        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting all running from Firebase", e)
            emptyList()
        }
    }

    // Generate a unique ID combining userId, timestamp, and random number
    private fun generateUniqueId(userId: String): String {
        val timestamp = System.currentTimeMillis()
        val random = (0..999999).random() // Random number between 0 and 999999
        return "${userId}_${timestamp}_${random}"
    }

    // Get calories burned by user
    suspend fun getHighestCaloriesBurnedByUser(userId: String): Int {
        return try {
            // Obter os documentos da coleção 'runningCollection' para o usuário
            val result = firestore.collection(CollectionsNames.runningCollection)
                .whereEqualTo(RunningCollection.FIELD_USER_ID, userId)
                .get()
                .await()

            // Verificar se o resultado não está vazio e mapear as calorias
            if (result.isEmpty) {
                0  // Se não houver documentos, retorna 0.0
            } else {
                result.documents.mapNotNull { document ->
                    // Tenta pegar o campo 'calories' e garantir que seja um número
                    (document[RunningCollection.FIELD_CALORIES] as Number).toInt()
                }.maxOrNull() ?: 0  // Retorna o maior valor ou 0.0 se não encontrar
            }
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting highest calories burned by user from Firebase", e)
            0  // Em caso de erro, retorna 0.0
        }
    }

    suspend fun getTotalExerciseTimeByUser(userId: String): Double {
        return try {
            val result = firestore.collection(CollectionsNames.runningCollection)
                .whereEqualTo(RunningCollection.FIELD_USER_ID, userId)
                .get()
                .await()

            if (result.isEmpty) {
                0.0
            } else {
                result.documents.sumOf { document ->
                    val durationString = document[RunningCollection.FIELD_DURATION] as? String
                    val duration = durationString?.toDoubleOrNull() ?: 0.0 // Converte para Double ou usa 0.0 se falhar

                    Log.d("RunningFirestoreRepository", "Duration found in document: $duration")

                    duration
                }
            }
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting total exercise time by user from Firebase", e)
            0.0
        }
    }

    // Get highest steps by user
    suspend fun getHighestStepsByUser(userId: String): Double {
        return try {
            val result = firestore.collection(CollectionsNames.runningCollection)
                .whereEqualTo(RunningCollection.FIELD_USER_ID, userId)
                .get()
                .await()

            if (result.isEmpty) {
                0.0
            } else {
                result.documents.mapNotNull { document ->
                    (document[RunningCollection.FIELD_STEPS] as? Number)?.toDouble()
                }.maxOrNull() ?: 0.0
            }
        } catch (e: Exception) {
            Log.d("RunningFirestoreRepository", "Error getting highest steps by user from Firebase", e)
            0.0
        }
    }
}
