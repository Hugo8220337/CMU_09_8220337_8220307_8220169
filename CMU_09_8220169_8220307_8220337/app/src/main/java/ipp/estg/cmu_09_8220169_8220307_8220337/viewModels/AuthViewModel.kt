package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.auth.AuthStatus
import ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences.UserPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepository(
        LocalDatabase.getDatabase(application).userDao
    )
    private val userPreferencesRepository: UserPreferencesRepository = UserPreferencesRepository(application)


    private val _authState: MutableStateFlow<AuthStatus> = MutableStateFlow(AuthStatus.LOADING)
    val authState: StateFlow<AuthStatus> get() = _authState

    var error by mutableStateOf("")

    init {
        // Check if user is logged
        viewModelScope.launch {
            _authState.value = authRepository.isLogged()
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _authState.value = AuthStatus.LOADING
        viewModelScope.launch {

            _authState.value = authRepository.login(email, password)
            if (_authState.value == AuthStatus.INVALID_LOGIN) {
                onError("Invalid login")
            }
            if (_authState.value == AuthStatus.LOGGED) {
                userPreferencesRepository.setCurrentUserId(authRepository.getCurrentUserId())
                onSuccess()
            }
        }
    }

    fun register(
        email: String,
        password: String,
        username: String,
        birthDate: String,
        weight: Double,
        height: Double,
        onError: (String) -> Unit = {}

    ) {
        try {
            if (email.isEmpty() || password.isEmpty() || username.isEmpty() || birthDate.isEmpty()) {
                error = "Please fill all fields"
                return
            }
            _authState.value = AuthStatus.LOADING

            viewModelScope.launch {
                _authState.value = authRepository.register(
                    email,
                    password,
                    username,
                    birthDate,
                    weight,
                    height
                )
            }
        } catch (e: Exception) {
            error = "Please fill all fields"
            onError(e.localizedMessage ?: "An error occurred")
            return
        }

    }

    fun logout() {
        _authState.value = AuthStatus.LOADING
        viewModelScope.launch {
            _authState.value = authRepository.logout()
        }
    }
}