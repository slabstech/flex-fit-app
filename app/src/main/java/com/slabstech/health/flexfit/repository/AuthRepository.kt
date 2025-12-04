// app/src/main/java/com/slabstech/health/flexfit/repository/AuthRepository.kt
package com.slabstech.health.flexfit.repository

import android.util.Log
import com.slabstech.health.flexfit.data.remote.ApiService
import com.slabstech.health.flexfit.data.remote.dto.RegisterRequest
import okio.IOException
import okhttp3.ResponseBody

private const val TAG = "AuthRepository"

class AuthRepository(private val api: ApiService) {

    suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            Log.d(TAG, "Register → $email")
            val response = api.register(RegisterRequest(username, email, password))

            if (response.isSuccessful) {
                Log.d(TAG, "Register success")
                Result.success(Unit)
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Log.e(TAG, "Register failed: ${response.code()} $error")
                Result.failure(Exception(parseErrorMessage(error, response.code())))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            Result.failure(Exception("No internet connection"))
        } catch (e: Exception) {
            Log.e(TAG, "Server returned invalid data", e)
            Result.failure(Exception("Server error. Try again later"))
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            Log.d(TAG, "Login → $email")
            val response = api.login(username = email, password = password)

            if (response.isSuccessful) {
                val bodyString = response.body()?.string() ?: ""
                Log.d(TAG, "Login raw response: $bodyString")

                val token = bodyString
                    .substringAfter("\"access_token\":\"", "")
                    .substringBefore("\"", "")
                    .takeIf { it.isNotBlank() && it.length > 20 }

                if (token != null) {
                    Log.d(TAG, "LOGIN SUCCESS – Token received (${token.take(20)}...)")
                    return Result.success(token)
                }
            }

            val error = response.errorBody()?.string() ?: "Unknown error"
            Log.e(TAG, "Login failed: ${response.code()} → $error")
            Result.failure(Exception("Wrong email or password"))

        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            Result.failure(Exception("Check your internet connection"))
        } catch (e: Exception) {
            Log.e(TAG, "Server error (HTML, crash, etc.)", e)
            Result.failure(Exception("Server error. Try again later"))
        }
    }

    private fun parseErrorMessage(errorBody: String, code: Int): String = when {
        errorBody.contains("already", ignoreCase = true) ||
                errorBody.contains("exists", ignoreCase = true) ||
                errorBody.contains("taken", ignoreCase = true) -> "Email or username already taken"

        errorBody.contains("password", ignoreCase = true) -> "Password too weak"
        code in 500..599 -> "Server error. Try again later"
        else -> "Registration failed"
    }
}