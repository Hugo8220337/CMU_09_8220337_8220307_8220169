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
    fun syncUserData() {
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
                _user.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


    /**
     * Get user info
     */
//    fun getUserInfo() {
//        _isLoading.value = true
//        _error.value = "" // Reset do estado de erro
//
//        viewModelScope.launch {
//            try {
//                val userInfo = repository.getUser()
//                if (userInfo != null) {
//                    _user.value = userInfo // Atualiza o estado com o usuário
//                } else {
//                    val errorMessage = "User not found."
//                    _error.value = errorMessage
//                }
//            } catch (e: Exception) {
//                val errorMessage = "Error retrieving user: ${e.message}"
//                _error.value = errorMessage
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

//    fun getUserInfo() {
//        _isLoading.value = true
//        _error.value = ""
//
//        viewModelScope.launch {
//            try {
//                // Primeiro tenta buscar do Room
//                val cachedUser = repository.getUserFromRoom()
//                if (cachedUser != null) {
//                    _user.value = cachedUser
//                } else {
//                    // Caso não exista no Room, busca no Firebase
//                    val userInfo = repository.getUserFromFirebase()
//                    if (userInfo != null) {
//                        _user.value = userInfo
//                    } else {
//                        _error.value = "User not found."
//                    }
//                }
//            } catch (e: Exception) {
//                _error.value = "Error retrieving user: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

//    fun getUserInfo() {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val cachedUser = repository.getCachedUser()
//                if (cachedUser != null) {
//                    _user.value = cachedUser
//                } else {
//                    repository.fetchAndSyncUser()
//                    _user.value = repository.getCachedUser()
//                }
//            } catch (e: Exception) {
//                _error.value = "Error: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    /**
     * Change user password
     */
//    fun changePassword(
//        email: String,
//        oldPassword: String,
//        newPassword: String
//    ) {
//        _isLoading.value = true
//        _error.value = "" // Reset the error state
//
//        viewModelScope.launch {
//            try {
//                val isPasswordChanged = repository.changePassword(email, oldPassword, newPassword)
//                if (isPasswordChanged) {
//                    // Notify success
//                } else {
//                    _error.value = "Password change failed."
//                }
//            } catch (e: Exception) {
//                _error.value = "Error: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//    fun changePassword(email: String, oldPassword: String, newPassword: String) {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val isSuccess = repository.changePassword(email, oldPassword, newPassword)
//                if (!isSuccess) _error.value = "Password change failed."
//            } catch (e: Exception) {
//                _error.value = "Error: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

}