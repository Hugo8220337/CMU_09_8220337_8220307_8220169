package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import ipp.estg.cmu_09_8220169_8220307_8220337.firebase.AuthenticationRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.firebase.UserFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: UserFirestoreRepository = UserFirestoreRepository()

    private val _user = MutableStateFlow<User?>(null)
    var user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow("")
    var error = _error.asStateFlow()


    /**
     * Get user info
     */
    fun getUserInfo() {
        _isLoading.value = true
        _error.value = "" // Reset do estado de erro

        viewModelScope.launch {
            try {
                val userInfo = repository.getUser()
                if (userInfo != null) {
                    _user.value = userInfo // Atualiza o estado com o usuário
                } else {
                    val errorMessage = "User not found."
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                val errorMessage = "Error retrieving user: ${e.message}"
                _error.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }



//    fun getUserInfo(
//        onSuccess: () -> Unit = {},
//        onError: (String) -> Unit = {}
//    ) {
//        _isLoading.value = true
//        try {
//
//        }
//        viewModelScope.launch {
//            try {
//
//                val userInfo = repository.getUser()
//                if (userInfo != null) {
//                    _user.value = userInfo
//                    onSuccess()
//                } else {
//                    onError("User not found")
//                    _error.value = "User not found"
//                }
//            } catch (e: Exception) {
//                _error.value = "Error: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

//    /*
//    * Register user
//    */
//    fun registUser(
//        user: User,
//        onSuccess: () -> Unit = {},
//        onError: (String) -> Unit = {}
//    ) {
//        _isLoading.value = true
//        _error.value = "" // Reset the error state
//
//        viewModelScope.launch {
//            try {
//                val isRegistered = repository.registUser(user)
//                if (isRegistered) {
//                    onSuccess()
//                } else {
//                    _error.value = "User registration failed."
//                    onError("User registration failed.")
//                }
//            } catch (e: Exception) {
//                _error.value = "Error: ${e.message}"
//                onError("Error: ${e.message}")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    /*
    * Register user
    */
    fun registUser(
        user: User,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _isLoading.value = true
        _error.value = "" // Reset the error state

        viewModelScope.launch {
            try {
                val isRegistered = repository.registUser(user)
                if (isRegistered) {
                    // Notify success
                } else {
                    _error.value = "User registration failed."
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

//    /**
//     * Change user password
//     */
//
//    fun changePassword(
//        email: String,
//        oldPassword: String,
//        newPassword: String,
//        onSuccess: () -> Unit = {},
//        onError: (String) -> Unit = {}
//    ){
//        _isLoading.value = true
//        _error.value = "" // Reset the error state
//
//        viewModelScope.launch {
//            try {
//                val isPasswordChanged = repository.changePassword(email, oldPassword, newPassword)
//                if (isPasswordChanged) {
//                    onSuccess()
//                } else {
//                    _error.value = "Password change failed."
//                    onError("Password change failed.")
//                }
//            } catch (e: Exception) {
//                _error.value = "Error: ${e.message}"
//                onError("Error: ${e.message}")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    /**
     * Change user password
     */
    fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String
    ) {
        _isLoading.value = true
        _error.value = "" // Reset the error state

        viewModelScope.launch {
            try {
                val isPasswordChanged = repository.changePassword(email, oldPassword, newPassword)
                if (isPasswordChanged) {
                    // Notify success
                } else {
                    _error.value = "Password change failed."
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}