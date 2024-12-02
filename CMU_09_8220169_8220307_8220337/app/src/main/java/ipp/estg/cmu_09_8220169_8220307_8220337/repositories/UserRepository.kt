package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.AuthFirebaeRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.UserFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.UserDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User

class UserRepository(
    private val userDao: UserDao,
    private val authFirebaseRepository: AuthFirebaeRepository
) {

    private val userFirestoreRepository: UserFirestoreRepository = UserFirestoreRepository()

    // Obter usuário do Room
    suspend fun getUserFromRoom(): User? {
        val userId = authFirebaseRepository.getCurrentUser()?.uid ?: return null
        return try {
            userDao.getUser(userId)
        } catch (e: Exception) {
            null
        }
    }

    // Obter utilizador do Firebase
    suspend fun getUserFromFirebase(): User? {
        val userId = authFirebaseRepository.getCurrentUser()?.uid ?: return null
        return userFirestoreRepository.getUserFromFirebase(userId)
    }

    // Salvar usuário no Room
    suspend fun saveUserToRoom(user: User): Long {
        return userDao.insertUser(user)
    }

    // Sincronizar dados do Firebase para Room
    suspend fun syncUserData() {
        val userFromFirebase = getUserFromFirebase()
        userFromFirebase?.let { userDao.insertUser(it) }
    }

    // Atualizar dados do usuário no Firebase
    suspend fun updateUserInFirebase(user: User) {
        userFirestoreRepository.updateUserInFirebase(user)

        // Atualizar dados do utilizador no Room
        updateUserInRoom(user)
    }

    // Atualizar dados do usuário no Room (usando insert com replace)
    suspend fun updateUserInRoom(user: User) {
        userDao.insertUser(user)
    }

}