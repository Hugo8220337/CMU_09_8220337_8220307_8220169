package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.auth.AuthStatus
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.UserCollection
import kotlinx.coroutines.tasks.await

class AuthFirebaseRepository(
    private val firebaseAuth: FirebaseAuth = Firebase.auth,
    private val firestore: FirebaseFirestore = Firebase.firestore
) {


    fun isLogged(): AuthStatus {
        return if (firebaseAuth.currentUser != null) {
            AuthStatus.LOGGED
        } else {
            AuthStatus.NO_LOGIN
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): AuthStatus {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result != null && result.user != null) {
                return AuthStatus.LOGGED
            }
        } catch (_: Exception) {
        }
        return AuthStatus.INVALID_LOGIN
    }

    suspend fun register(
        email: String,
        password: String,
        username: String,
        birthDate: String,
        weight: Double,
        height: Double
    ): AuthStatus {
        return try {
            // Insert user in firebase Auth
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (result != null && result.user != null) {

                // Use UID as Firestore document ID
                val userId = result.user!!.uid

                // Insert user on firestore User Collection
                val user = hashMapOf(
                    UserCollection.FIELD_ID  to userId,
                    UserCollection.FIELD_NAME to username,
                    UserCollection.FIELD_BIRTH_DATE to birthDate,
                    UserCollection.FIELD_WEIGHT to weight,
                    UserCollection.FIELD_HEIGHT to height
                )

                firestore.collection(CollectionsNames.userCollection)
                    .document(userId) // Use UID as document ID
                    .set(user) // Use .set() instead of .add()
                    .await()

                // return logged status
                AuthStatus.LOGGED
            } else {
                // return invalid login status
                AuthStatus.INVALID_LOGIN
            }
        } catch (e: Exception) {
            Log.e("AuthFirebaseRepository", "Error on register", e)
            // return invalid login status
            AuthStatus.INVALID_LOGIN
        }
    }

    fun getCurrentUser() = firebaseAuth.currentUser


    fun logout(): AuthStatus {
        firebaseAuth.signOut()
        return AuthStatus.NO_LOGIN
    }
}