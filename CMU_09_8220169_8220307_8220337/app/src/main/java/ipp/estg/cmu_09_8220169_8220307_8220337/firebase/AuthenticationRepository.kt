package ipp.estg.cmu_09_8220169_8220307_8220337.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.AuthStatus
import kotlinx.coroutines.tasks.await

class AuthenticationRepository {
    private val firebaseAuth: FirebaseAuth = Firebase.auth

    fun isLogged(): AuthStatus {
        return if(firebaseAuth.currentUser != null){
            AuthStatus.LOGGED
        }else{
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
        password: String
    ): AuthStatus {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (result != null && result.user != null) {
                return AuthStatus.LOGGED
            }
        } catch (_: Exception) {
        }
        return AuthStatus.INVALID_LOGIN
    }

    fun logout(): AuthStatus {
        firebaseAuth.signOut()
        return AuthStatus.NO_LOGIN
    }
}