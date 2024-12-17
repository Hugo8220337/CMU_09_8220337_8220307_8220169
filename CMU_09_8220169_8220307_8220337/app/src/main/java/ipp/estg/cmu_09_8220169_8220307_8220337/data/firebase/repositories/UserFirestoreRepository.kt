package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.UserCollection
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

    private val authFirebaseRepository: AuthFirebaseRepository = AuthFirebaseRepository()

    // Get user from Firebase
    suspend fun getUserFromFirebase(userId: String): User? {
        return try {
            val result = firestore.collection(CollectionsNames.userCollection)
                .whereEqualTo("id", userId)
                .get()
                .await()

            if (result != null && !result.isEmpty) {
                val document = result.documents.first()
                User(
                    id = document.getString("id") ?: "",
                    name = document.getString("name") ?: "",
                    birthDate = document.getString("birthDate") ?: "",
                    weight = document.getDouble("weight") ?: 0.0,
                    height = document.getDouble("height") ?: 0.0
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    // Update user information in Firebase
    suspend fun updateUserInFirebase(user: User) {
        try {
            //val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
            val userId = authFirebaseRepository.getCurrentUser()?.uid ?: return
            val updates = mapOf(
                //UserCollection.FIELD_ID to user.id,
                UserCollection.FIELD_NAME to user.name,
                UserCollection.FIELD_BIRTH_DATE to user.birthDate,
                UserCollection.FIELD_WEIGHT to user.weight,
                UserCollection.FIELD_HEIGHT to user.height
            )
            firestore.collection(CollectionsNames.userCollection)
                .document(userId)
                .set(updates, SetOptions.merge()) // Usa merge para manter outros campos inalterados
                .await()

            Log.d("UpdateUser", "User updated successfully")
        } catch (e: Exception) {
            // Handle the exception (e.g., log the error)
            Log.e("UpdateUserError", "Failed to update user: ${e.message}")
        }
    }

    //get all users from Firebase, return a list of users
    suspend fun getAllUsers(): List<User> {
        return try {

            val result = firestore.collection(CollectionsNames.userCollection)
                .get()
                .await()

            Log.d("UserFirestoreRepository", "Número de documentos: ${result.documents.size}")

            if (result.isEmpty) {
                // Caso não haja documentos
                return emptyList()
            } else {
                result.documents.map { document ->
                    Log.d("UserFirestoreRepository", "Document: ${document.data}")

                    val id = document.get("id")?.let { it.toString() }
                        ?: ""  // Convertendo o valor para String, independente do tipo
                    val name = document.getString("name") ?: ""

                    // Retorna um par (id, name) para cada usuário
                    Pair(id, name)
                }
            }.map { (id, name) ->
                User(id = id, name = name)
            }
        } catch (e: Exception) {
            Log.e("UserFirestoreRepository", "Error getting users: ${e.message}")
            emptyList()
        }
    }

    suspend fun updateFirstRun() {
        val userId = authFirebaseRepository.getCurrentUser()?.uid ?: return
        try {
            val updates = mapOf(
                UserCollection.FIELD_IS_FIRST_RUN to false
            )
            firestore.collection(CollectionsNames.userCollection)
                .document(userId)
                .set(updates, SetOptions.merge()) // Usa merge para manter outros campos inalterados
                .await()

            Log.d("UpdateFirstRun", "First run updated successfully")
        } catch (e: Exception) {
            // Handle the exception (e.g., log the error)
            Log.e("UpdateFirstRunError", "Failed to update first run: ${e.message}")
        }
    }
}