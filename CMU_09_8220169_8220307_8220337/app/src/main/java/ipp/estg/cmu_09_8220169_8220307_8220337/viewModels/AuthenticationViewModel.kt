package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.AuthStatus
import ipp.estg.cmu_09_8220169_8220307_8220337.firebase.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository: AuthenticationRepository = AuthenticationRepository()

    private val _authState: MutableStateFlow<AuthStatus> = MutableStateFlow(AuthStatus.LOADING)
    val authState: StateFlow<AuthStatus> get() = _authState

    var error by mutableStateOf("")

    init {
        viewModelScope.launch {
            _authState.value = repository.isLogged()
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        _authState.value = AuthStatus.LOADING
        viewModelScope.launch {
            _authState.value = repository.login(email, password)
        }
    }

    fun register(
        email: String,
        password: String
    ) {
        _authState.value = AuthStatus.LOADING
        viewModelScope.launch {
            _authState.value = repository.register(email, password)
        }
    }

    fun logout() {
        _authState.value = AuthStatus.LOADING
        viewModelScope.launch {
            _authState.value = repository.logout()
        }
    }
}