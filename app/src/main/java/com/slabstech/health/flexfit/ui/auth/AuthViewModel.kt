// File: app/src/main/java/com/slabstech/health/flexfit/ui/auth/AuthViewModel.kt
package com.slabstech.health.flexfit.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.LoginRequest
import com.slabstech.health.flexfit.data.remote.dto.RegisterRequest
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

    private val authRepository = AuthRepository(application)

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    // FIXED: Use proper block body, not expression
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
        if (!isLoginInputValid()) {
            _state.value = _state.value.copy(error = "Please enter a valid email and password")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            authRepository.login(LoginRequest(username = _state.value.email, password = _state.value.password))
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
        if (!isRegisterInputValid()) {
            _state.value = _state.value.copy(error = "Please fill all fields correctly")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            authRepository.register(
                RegisterRequest(
                    username = _state.value.username,
                    email = _state.value.email,
                    password = _state.value.password
                )
            )
                .onSuccess { login(onSuccess) }
                .onFailure {
                    _state.value = _state.value.copy(error = "Email already registered")
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun isLoginInputValid(): Boolean {
        val email = _state.value.email
        return email.isNotBlank() &&
                _state.value.password.isNotBlank() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isRegisterInputValid(): Boolean {
        return _state.value.username.isNotBlank() &&
                _state.value.email.isNotBlank() &&
                _state.value.password.length >= 6 &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(_state.value.email).matches()
    }
}