// File: app/src/main/java/com/slabstech/health/flexfit/repository/AuthRepository.kt
package com.slabstech.health.flexfit.repository

import com.slabstech.health.flexfit.data.remote.ApiService
import com.slabstech.health.flexfit.data.remote.dto.RegisterRequest
import com.slabstech.health.flexfit.utils.TokenManager

class AuthRepository(private val api: ApiService) {

    suspend fun login(email: String, password: String): Result<String> = try {
        val response = api.login(username = email, password = password)
        if (response.isSuccessful) {
            val token = response.body()?.access_token.orEmpty()
            if (token.isNotBlank()) {
                Result.success(token)
            } else {
                Result.failure(Exception("Empty token"))
            }
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun register(username: String, email: String, password: String): Result<Unit> = try {
        val response = api.register(RegisterRequest(username, email, password))
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            val error = response.errorBody()?.string() ?: "Registration failed"
            Result.failure(Exception(error))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}