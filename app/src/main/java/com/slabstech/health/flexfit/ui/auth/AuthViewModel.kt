// app/src/main/java/com/slabstech/health/flexfit/ui/auth/AuthViewModel.kt
package com.slabstech.health.flexfit.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.RetrofitClient
import com.slabstech.health.flexfit.repository.AuthRepository
import com.slabstech.health.flexfit.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AuthViewModel"

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

    private fun isRegisterInputValid(): Boolean {
        val s = _state.value
        return s.username.isNotBlank() &&
                s.email.isNotBlank() &&
                s.password.length >= 6 &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches()
    }

    fun register(onSuccess: () -> Unit) {
        val s = _state.value
        Log.d(TAG, "=== REGISTER ATTEMPT ===")
        Log.d(TAG, "Username: '${s.username}' | Email: '${s.email}' | Pass: ${s.password.length} chars")

        if (!isRegisterInputValid()) {
            _state.value = _state.value.copy(error = "Invalid username, email, or password (min 6 chars)")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            authRepository.register(s.username, s.email, s.password)
                .onSuccess {
                    Log.d(TAG, "REGISTER SUCCESS → Auto-login")
                    login(onSuccess)
                }
                .onFailure { e ->
                    Log.e(TAG, "REGISTER FAILED", e)
                    _state.value = _state.value.copy(error = e.message ?: "Registration failed")
                }
                .also { _state.value = _state.value.copy(isLoading = false) }
        }
    }

    fun login(onSuccess: () -> Unit) {
        val s = _state.value
        Log.d(TAG, "=== LOGIN ATTEMPT === Email: '${s.email}'")

        if (s.email.isBlank() || s.password.isBlank()) {
            _state.value = _state.value.copy(error = "Fill email and password")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            authRepository.login(s.email, s.password)
                .onSuccess { token ->
                    Log.d(TAG, "LOGIN SUCCESS! Token saved (${token.length} chars)")
                    TokenManager.saveToken(getApplication(), token)
                    onSuccess()
                }
                .onFailure { e ->
                    Log.e(TAG, "LOGIN FAILED", e)
                    _state.value = _state.value.copy(error = "Wrong email or password")
                }
                .also { _state.value = _state.value.copy(isLoading = false) }
        }
    }
}