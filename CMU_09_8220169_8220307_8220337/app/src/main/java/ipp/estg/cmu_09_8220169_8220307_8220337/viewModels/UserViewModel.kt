package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.AuthFirebaseRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.UserFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
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
        AuthFirebaseRepository()
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
    fun getUser(
        onSuccess: (user: User) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val user = userRepository.getUserById()

                _user.value = user

                if (user != null) {
                    onSuccess(user)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                onError(_errorMessage.value!!)
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
                userRepository.updateUser(user)

                // Atualiza o estado do utilizador
                _user.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateFirstRun() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                userRepository.updateFirstRun()

            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}