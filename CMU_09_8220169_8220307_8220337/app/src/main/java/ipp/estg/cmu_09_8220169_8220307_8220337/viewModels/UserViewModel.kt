package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Dao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.AuthFirebaeRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.UserFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.UserDao
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val userFirestoreRepository: UserFirestoreRepository = UserFirestoreRepository()

    private val userRepository: UserRepository = UserRepository(
        LocalDatabase.getDatabase(application).userDao,
        AuthFirebaeRepository()
    )

    // Estado de usuário
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()


    // Estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Estado de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Função para obter o usuário do Room ou Firebase
    fun getUser() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val userFromRoom = userRepository.getUserFromRoom()

                // Se não houver usuário no Room, tentar buscar no Firebase
                if (userFromRoom == null) {
                    val userFromFirebase = userRepository.getUserFromFirebase()

                    // Se encontrar no Firebase, salva no Room
                    userFromFirebase?.let {
                        userRepository.saveUserToRoom(it)
                    }

                    _user.value = userFromFirebase
                } else {
                    _user.value = userFromRoom
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Função para sincronizar os dados do Firebase para o Room
    private fun syncUserData() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                userRepository.syncUserData()
                getUser() // Depois de sincronizar, obter novamente o usuário
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Função para atualizar os dados do usuário no Firebase e Room
    fun updateUser(user: User) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                userRepository.updateUserInFirebase(user)
                userRepository.updateUserInRoom(user)

                // Atualiza o estado do utilizador
                _user.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}