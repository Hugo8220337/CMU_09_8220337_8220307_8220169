package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories

import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.UserCollection
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository {

    private val db: FirebaseFirestore = Firebase.firestore
    private val firebaseAuth: FirebaseAuth = Firebase.auth


    suspend fun getUser(): User? {
        val currentUser = firebaseAuth.currentUser
        return try {
            if (currentUser != null) {
                // Procura o user autenticado na coleção de user
                val result = db.collection(CollectionsNames.userCollection)
                    .whereEqualTo(UserCollection.FIELD_ID, currentUser.uid)
                    .limit(1)
                    .get()
                    .await()

                // Converte o primeiro documento encontrado para o modelo User
                if (!result.isEmpty) {
                    result.documents[0].toObject(User::class.java)
                } else {
                    null // Nenhum user encontrado
                }
            } else {
                null // Nenhum user autenticado
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Retorna null em caso de erro
        }
    }



    suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ): Boolean {
        val currentUser = firebaseAuth.currentUser

        if (currentUser == null || currentUser.email != email) {
            return false // Usuário não autenticado ou e-mail não corresponde
        }

        return try {
            // Reautentica o usuário antes de alterar a senha
            currentUser.reauthenticate(
                EmailAuthProvider.getCredential(email, oldPassword)
            ).await()

            // Atualiza a senha do usuário
            currentUser.updatePassword(newPassword).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false // Retorna false em caso de erro
        }
    }

}