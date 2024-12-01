package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories

import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.UserCollection
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.UserDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

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
                    email = document.getString("email") ?: "",
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
            val updates = mapOf(
                "name" to user.name,
                "email" to user.email,
                "birthDate" to user.birthDate,
                "weight" to user.weight,
                "height" to user.height
            )
            firestore.collection(CollectionsNames.userCollection)
                .document(user.id)
                .update(updates)
                .await()
        } catch (e: Exception) {
            // Handle the exception (e.g., log the error)
        }
    }

//    private val db: FirebaseFirestore = Firebase.firestore
//    private val firebaseAuth: FirebaseAuth = Firebase.auth
//
//
////    suspend fun getUser(): User? {
////        val currentUser = firebaseAuth.currentUser
////        return try {
////            if (currentUser != null) {
////                // Procura o user autenticado na coleção de user
////                val result = db.collection(CollectionsNames.userCollection)
////                    .whereEqualTo(UserCollection.FIELD_ID, currentUser.uid)
////                    .limit(1)
////                    .get()
////                    .await()
////
////                // Converte o primeiro documento encontrado para o modelo User
////                if (!result.isEmpty) {
////                    result.documents[0].toObject(User::class.java)
////                } else {
////                    null // Nenhum user encontrado
////                }
////            } else {
////                null // Nenhum user autenticado
////            }
////        } catch (e: Exception) {
////            e.printStackTrace()
////            null // Retorna null em caso de erro
////        }
////    }
//
////    suspend fun getUser(): User? {
////        val currentUser = firebaseAuth.currentUser
////        return try {
////            if (currentUser != null) {
////                // Busca no Firebase
////                val result = db.collection(CollectionsNames.userCollection)
////                    .whereEqualTo(UserCollection.FIELD_ID, currentUser.uid)
////                    .limit(1)
////                    .get()
////                    .await()
////
////                if (!result.isEmpty) {
////                    val user = result.documents[0].toObject(User::class.java)
////                    user?.let { userDao.insertUser(it) } // Salva no Room
////                    user
////                } else {
////                    null
////                }
////            } else {
////                null
////            }
////        } catch (e: Exception) {
////            e.printStackTrace()
////            null
////        }
////    }
//
//    suspend fun getCachedUser(): User? {
//        return userDao.getUser(firebaseAuth.currentUser?.uid ?: "")
//    }
//
//    suspend fun fetchUserFromFirebase(): User? {
//        val userId = firebaseAuth.currentUser?.uid ?: return null
//        return try {
//            val result = db.collection(CollectionsNames.userCollection)
//                .document(userId)
//                .get()
//                .await()
//
//            result.toObject(User::class.java)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    suspend fun fetchAndSyncUser() {
//        val user = fetchUserFromFirebase()
//        user?.let { userDao.insertUser(it) }
//    }
//
////    suspend fun changePassword(
////        email: String,
////        oldPassword: String,
////        newPassword: String
////    ): Boolean {
////        val currentUser = firebaseAuth.currentUser
////
////        if (currentUser == null || currentUser.email != email) {
////            return false // Usuário não autenticado ou e-mail não corresponde
////        }
////
////        return try {
////            // Reautentica o usuário antes de alterar a senha
////            currentUser.reauthenticate(
////                EmailAuthProvider.getCredential(email, oldPassword)
////            ).await()
////
////            // Atualiza a senha do usuário
////            currentUser.updatePassword(newPassword).await()
////            true
////        } catch (e: Exception) {
////            e.printStackTrace()
////            false // Retorna false em caso de erro
////        }
////    }
//
//
//    suspend fun getUserFromRoom(): User? {
//        return userDao.getUser(firebaseAuth.currentUser?.uid ?: "")
//    }
//
//    suspend fun getUserFromFirebase(): User? {
//        val firestore = FirebaseFirestore.getInstance()
//        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return null
//
//        return try {
//            val documentSnapshot = firestore.collection("users")
//                .document(userId)
//                .get()
//                .await()
//
//            if (documentSnapshot.exists()) {
//                documentSnapshot.toObject(User::class.java)
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
////    suspend fun fetchAndSyncUser() {
////        val user = getUserFromFirebase() // Busca o usuário do Firebase
////        syncUserWithRoom(user, userDao)  // Sincroniza com o banco de dados local
////    }
//
//
//    private suspend fun syncUserWithRoom(user: User?, userDao: UserDao) {
//        user?.let {
//            userDao.insertUser(it) // Insere o usuário no banco de dados local
//        }
//    }
//
//    suspend fun changePassword(email: String, oldPassword: String, newPassword: String): Boolean {
//        val currentUser = firebaseAuth.currentUser ?: return false
//        if (currentUser.email != email) return false
//
//        return try {
//            currentUser.reauthenticate(EmailAuthProvider.getCredential(email, oldPassword)).await()
//            currentUser.updatePassword(newPassword).await()
//            true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }

}