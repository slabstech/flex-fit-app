// app/src/main/java/com/slabstech/health/flexfit/repository/AuthRepository.kt
package com.slabstech.health.flexfit.repository

import android.util.Log
import com.slabstech.health.flexfit.data.remote.ApiService
import com.slabstech.health.flexfit.data.remote.dto.RegisterRequest
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "AuthRepository"

class AuthRepository(private val api: ApiService) {

    suspend fun register(username: String, email: String, password: String): Result<Unit> = try {
        Log.d(TAG, "=== API CALL: POST /register/ ===")
        Log.d(TAG, "Sending → username='$username', email='$email', password_length=${password.length}")

        val response = api.register(RegisterRequest(username, email, password))

        if (response.isSuccessful) {
            Log.d(TAG, "REGISTER SUCCESS! Response: ${response.body()}")
            Result.success(Unit)
        } else {
            val errorCode = response.code()
            val errorBody = response.errorBody()?.string() ?: "No error body"
            Log.e(TAG, "REGISTER FAILED → HTTP $errorCode")
            Log.e(TAG, "Error body: $errorBody")

            // Extract user-friendly message
            val userMessage = when {
                errorBody.contains("already registered", ignoreCase = true) ||
                        errorBody.contains("Email already", ignoreCase = true) -> "Email already taken"

                errorBody.contains("username", ignoreCase = true) -> "Username already taken"
                else -> "Registration failed. Try again."
            }

            Result.failure(Exception(userMessage))
        }
    } catch (e: HttpException) {
        Log.e(TAG, "HTTP Exception during register: ${e.code()} ${e.message()}")
        Result.failure(e)
    } catch (e: IOException) {
        Log.e(TAG, "Network error (no internet?): ${e.message}")
        Result.failure(Exception("No internet connection"))
    } catch (e: Exception) {
        Log.e(TAG, "Unexpected error during register", e)
        Result.failure(e)
    }

    suspend fun login(email: String, password: String): Result<String> = try {
        Log.d(TAG, "=== API CALL: POST /login ===")
        Log.d(TAG, "Sending → email='$email', password_length=${password.length}")

        val response = api.login(username = email, password = password)

        if (response.isSuccessful) {
            val token = response.body()?.access_token.orEmpty()
            if (token.isNotBlank()) {
                Log.d(TAG, "LOGIN SUCCESS! Token received (${token.length} chars)")
                Result.success(token)
            } else {
                Log.e(TAG, "Token is empty!")
                Result.failure(Exception("Empty token"))
            }
        } else {
            val errorCode = response.code()
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            Log.e(TAG, "LOGIN FAILED → HTTP $errorCode | Body: $errorBody")
            Result.failure(Exception("Wrong email or password"))
        }
    } catch (e: HttpException) {
        Log.e(TAG, "HTTP Exception during login: ${e.code()} ${e.message()}")
        Result.failure(e)
    } catch (e: IOException) {
        Log.e(TAG, "Network error during login: ${e.message}")
        Result.failure(Exception("Check your internet connection"))
    } catch (e: Exception) {
        Log.e(TAG, "Unexpected error during login", e)
        Result.failure(e)
    }
}