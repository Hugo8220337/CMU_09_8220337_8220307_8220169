package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.AuthFirebaseRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.UserFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.UserDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User

class UserRepository(
    private val userDao: UserDao,
    private val authFirebaseRepository: AuthFirebaseRepository
) {

    private val userFirestoreRepository: UserFirestoreRepository = UserFirestoreRepository()

    // Obter usuário do Room
    private suspend fun getUserFromRoom(): User? {
        val userId = authFirebaseRepository.getCurrentUser()?.uid ?: return null
        return try {
            userDao.getUser(userId)
        } catch (e: Exception) {
            null
        }
    }

    // Obter utilizador do Firebase
    private suspend fun getUserFromFirebase(): User? {
        val userId = authFirebaseRepository.getCurrentUser()?.uid ?: return null
        return userFirestoreRepository.getUserFromFirebase(userId)
    }

    // Salvar usuário no Room
    suspend fun saveUserToRoom(user: User): Long {
        return userDao.insertUser(user)
    }

    // Sincronizar dados do Firebase para Room
    private suspend fun syncUserData() {
        val userFromFirebase = getUserFromFirebase()
        userFromFirebase?.let { userDao.insertUser(it) }
    }

    //Sincronizar dados do Room para Firebase
    private suspend fun syncUserDataToFirebase() {
        val userFromRoom = getUserFromRoom()
        userFromRoom?.let { userFirestoreRepository.updateUserInFirebase(it) }
    }

    // Atualizar dados do utilizador no Firebase
    private suspend fun updateUserInFirebase(user: User) {
        userFirestoreRepository.updateUserInFirebase(user)

        // Atualizar dados do utilizador no Room
        updateUserInRoom(user)
    }

    // Atualizar dados do usurious no Room (usando insert com replace)
    private suspend fun updateUserInRoom(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUserById(): User? {

        val userId = authFirebaseRepository.getCurrentUser()?.uid ?: return null
        var user = userFirestoreRepository.getUserFromFirebase(userId)

        // se não existir no firebase, tenta buscar do Room
        if (user == null) {
            user = getUserFromRoom()
            // sincroniza os dados do Room para o Firebase
            syncUserDataToFirebase()
        }

        // sincroniza os dados do Firebase para o Room
        syncUserData()

        // Retorna um novo objeto User com o email do utilizador atual
        if (user != null) {
            user  = User(
                name = user.name,
                email = authFirebaseRepository.getCurrentUser()?.email ?: "",
                birthDate = user.birthDate,
                weight = user.weight,
                height = user.height
            )
        }

        return user
    }

    suspend fun updateFirstRun() {
        userFirestoreRepository.updateFirstRun()
    }


    suspend fun updateUser(user: User) {
        updateUserInFirebase(user)
    }
}