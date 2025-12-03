// repository/AuthRepository.kt
package com.slabstech.health.flexfit.repository


import android.content.Context
import com.slabstech.health.flexfit.data.remote.dto.LoginRequest
import com.slabstech.health.flexfit.data.remote.dto.RegisterRequest


class AuthRepository(private val context: Context) {
    private val api = RetrofitClient.getApiService(context)

    suspend fun login(request: LoginRequest): Result<String> = try {
        val response = api.login(request)
        if (response.isSuccessful) {
            Result.success(response.body()?.access_token ?: "")
        } else {
            Result.failure(Exception("Login failed"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun register(request: RegisterRequest): Result<Unit> = try {
        val response = api.register(request)
        if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Register failed"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}