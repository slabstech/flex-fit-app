// File: app/src/main/java/com/slabstech/health/flexfit/ui/auth/AuthViewModel.kt
package com.slabstech.health.flexfit.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.repository.AuthRepository
import com.slabstech.health.flexfit.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(RetrofitClient.getApiService(application))

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email.trim(), error = null)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password, error = null)
    }

    fun updateUsername(username: String) {
        _state.value = _state.value.copy(username = username.trim(), error = null)
    }

    fun login(onSuccess: () -> Unit) {
        if (_state.value.email.isBlank() || _state.value.password.isBlank()) {
            _state.value = _state.value.copy(error = "Please fill email and password")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            authRepository.login(_state.value.email, _state.value.password)
                .onSuccess { token ->
                    TokenManager.saveToken(getApplication(), token)
                    onSuccess()
                }
                .onFailure {
                    _state.value = _state.value.copy(error = "Wrong email or password")
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun register(onSuccess: () -> Unit) {
        if (_state.value.username.isBlank() || _state.value.email.isBlank() || _state.value.password.length < 6) {
            _state.value = _state.value.copy(error = "Fill all fields correctly")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            authRepository.register(_state.value.username, _state.value.email, _state.value.password)
                .onSuccess {
                    login(onSuccess) // Auto-login after register
                }
                .onFailure {
                    _state.value = _state.value.copy(error = "Email already taken")
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}