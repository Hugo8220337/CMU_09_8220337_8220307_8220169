package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.auth.AuthStatus
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.AuthFirebaseRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.UserDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User

class AuthRepository(
    private val userDao: UserDao
) {
    private val authFirebaseRepository: AuthFirebaseRepository = AuthFirebaseRepository()

    suspend fun login(email: String, password: String): AuthStatus {
        val result = authFirebaseRepository.login(email, password)
        return result
    }

    suspend fun register(
        email: String,
        password: String,
        username: String,
        birthDate: String,
        weight: Double,
        height: Double
    ): AuthStatus {
        val result =
            authFirebaseRepository.register(email, password, username, birthDate, weight, height)

        // If user is logged, insert user in Room
        if (result == AuthStatus.LOGGED) {
            val userId = authFirebaseRepository.getCurrentUser()?.uid ?: return AuthStatus.INVALID_LOGIN
            insertUserInRoom(userId, username, email, birthDate, weight, height)
        }

        return result
    }

    suspend fun isLogged(): AuthStatus {
        return authFirebaseRepository.isLogged()
    }

    suspend fun logout(): AuthStatus {
        return authFirebaseRepository.logout()
    }

    private suspend fun insertUserInRoom(
        userId: String,
        username: String,
        email: String,
        birthDate: String,
        weight: Double,
        height: Double
    ) {
        val newRoomUser = User(
            id = userId,
            name = username,
            email = email,
            birthDate = birthDate,
            weight = weight,
            height = height
        )

        userDao.insertUser(newRoomUser)
    }

    fun getCurrentUserId(): String {
        return authFirebaseRepository.getCurrentUser()?.uid ?: ""
    }
}