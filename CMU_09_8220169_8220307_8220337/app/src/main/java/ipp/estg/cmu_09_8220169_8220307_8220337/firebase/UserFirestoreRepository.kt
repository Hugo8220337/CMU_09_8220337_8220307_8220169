package ipp.estg.cmu_09_8220169_8220307_8220337.firebase

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.ActionCodeEmailInfo
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.models.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository() {

    private val db: FirebaseFirestore
    private val firebaseAuth: FirebaseAuth

    init {
        db = Firebase.firestore
        firebaseAuth = Firebase.auth
    }

//    suspend fun getUser(): User? {
//        val user = firebaseAuth.currentUser
//        return if (user != null) {
//            val result = db.collection(CollectionsNames.userCollection.collectionName)
//                .whereEqualTo(CollectionsNames.userCollection.fieldEmail, user.email)
//                .get()
//                .await()
//
//            if (!result.isEmpty) {
//                result.documents[0].toObject(User::class.java)
//            } else {
//                null
//            }
//        } else {
//            null
//        }
//    }

//    suspend fun getUser(): User? {
//        var user: User? = null
//
//        try {
//            val queryReference =
//                db.collection(CollectionsNames.userCollection.collectionName)
//                    .whereEqualTo(
//                        CollectionsNames.userCollection.fieldEmail,
//                        firebaseAuth.currentUser!!.email
//                    ).limit(1)
//
//            val result = queryReference.get().await()
//
//            if (!result.isEmpty) {
//                user = result.documents[0].toObject(User::class.java)
//            }
//        }catch (e: Exception){
//            user = null
//        }
//
//        return user
//    }

    /**
     * Obtém o usuário atual autenticado no Firestore com base no e-mail.
     * Retorna o objeto User se encontrado, ou null caso contrário.
     */
    suspend fun getUser(): User? {
        val currentUser = firebaseAuth.currentUser
        return try {
            if (currentUser != null) {
                // Consulta a coleção de usuários filtrando pelo e-mail
                val result = db.collection(CollectionsNames.userCollection.collectionName)
                    .whereEqualTo(CollectionsNames.userCollection.fieldEmail, currentUser.email)
                    .limit(1)
                    .get()
                    .await()

                // Converte o primeiro documento encontrado para o modelo User
                if (!result.isEmpty) {
                    result.documents[0].toObject(User::class.java)
                } else {
                    null // Nenhum usuário encontrado
                }
            } else {
                null // Nenhum usuário autenticado
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Retorna null em caso de erro
        }
    }

//    //register user
//    suspend fun registUser(user: User): Boolean {
//        return try {
//            val result = db.collection(CollectionsNames.userCollection.collectionName)
//                .whereEqualTo(CollectionsNames.userCollection.fieldEmail, user.email)
//                .get()
//                .await()
//
//            if (result.isEmpty) {
//                // Adiciona o utilizador no Firestore
//                db.collection(CollectionsNames.userCollection.collectionName)
//                    .add(user)
//                    .await()
//                true
//            } else {
//                false // utilizador já existe
//            }
//        } catch (e: Exception) {
//            false
//        }
//    }

    /**
     * Registra um novo usuário no Firestore.
     * Retorna true se o usuário foi registrado com sucesso, ou false se já existe ou ocorreu erro.
     */
    suspend fun registUser(user: User): Boolean {
        return try {
            val result = db.collection(CollectionsNames.userCollection.collectionName)
                .whereEqualTo(CollectionsNames.userCollection.fieldEmail, user.email)
                .get()
                .await()

            if (result.isEmpty) {
                // Adiciona o usuário no Firestore
                db.collection(CollectionsNames.userCollection.collectionName)
                    .add(user)
                    .await()
                true
            } else {
                false // Usuário já existe
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false // Retorna false em caso de erro
        }
    }

//    suspend fun changePassword(
//        email: String,
//        oldPassword : String,
//        newPassword: String,
//    ):Boolean{
//        var user = firebaseAuth.currentUser
//
//        if (user == null || !firebaseAuth.currentUser!!.email.equals(email)) {
//            return false
//        }
//
//        var isSuccessful = false
//
//        try {
//            user.reauthenticateAndRetrieveData(
//                EmailAuthProvider.getCredential(
//                    email,
//                    oldPassword
//                )
//            ).await()
//
//            user.updatePassword(newPassword).await()
//
//            isSuccessful = true
//
//        }catch (e: Exception){
//            isSuccessful = false
//        }
//
//        return isSuccessful
//    }

    /**
     * Altera a senha do usuário autenticado no Firebase Authentication.
     * Retorna true se a senha foi alterada com sucesso, ou false em caso de erro.
     */
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