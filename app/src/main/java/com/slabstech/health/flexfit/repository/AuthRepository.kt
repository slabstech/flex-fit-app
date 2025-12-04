// app/src/main/java/com/slabstech/health/flexfit/repository/AuthRepository.kt
package com.slabstech.health.flexfit.repository

import android.util.Log
import com.slabstech.health.flexfit.data.remote.ApiService
import com.slabstech.health.flexfit.data.remote.dto.RegisterRequest
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import okio.IOException
import retrofit2.Response

private const val TAG = "AuthRepository"

class AuthRepository(private val api: ApiService) {

    suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            Log.d(TAG, "=== API CALL: POST /register/ ===")
            Log.d(TAG, "Sending → username='$username', email='$email', password_length=${password.length}")

            val response = api.register(RegisterRequest(username, email, password))

            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "REGISTER SUCCESS! User created.")
                Result.success(Unit)
            } else {
                handleError(response)
            }
        } catch (e: JsonDataException) {
            Log.e(TAG, "JSON parsing failed – server returned malformed data (likely HTML)", e)
            Result.failure(Exception("Server error. Please try again later"))
        } catch (e: JsonEncodingException) {
            Log.e(TAG, "JSON encoding error from server", e)
            Result.failure(Exception("Server error. Please try again later"))
        } catch (e: IOException) {
            Log.e(TAG, "REAL network failure (no internet, timeout, etc.)", e)
            Result.failure(Exception("No internet connection"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during register", e)
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            Log.d(TAG, "=== API CALL: POST /login ===")
            Log.d(TAG, "Sending → email='$email', password_length=${password.length}")

            val response = api.login(username = email, password = password)

            if (response.isSuccessful) {
                val token = response.body()?.accessToken.orEmpty()
                if (token.isNotBlank()) {
                    Log.d(TAG, "LOGIN SUCCESS! Token received (${token.length} chars)")
                    return Result.success(token)
                }
            }

            // Login failed
            handleError(response)
        } catch (e: JsonDataException) {
            Log.e(TAG, "JSON parsing failed on login", e)
            Result.failure(Exception("Server error. Please try again"))
        } catch (e: JsonEncodingException) {
            Log.e(TAG, "JSON encoding issue", e)
            Result.failure(Exception("Server error. Please try again"))
        } catch (e: IOException) {
            Log.e(TAG, "REAL network failure", e)
            Result.failure(Exception("Check your internet connection"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during login", e)
            Result.failure(e)
        }
    }

    // Generic error handler – works for both Unit and String results
    private fun <T> handleError(response: Response<T>): Result<T> {
        val code = response.code()
        val errorBody = try {
            response.errorBody()?.string() ?: "No error body"
        } catch (e: Exception) {
            "Failed to read error body"
        }

        Log.e(TAG, "API FAILED → HTTP $code | Body: $errorBody")

        val userMessage = when {
            code in 500..599 -> "Server error. Please try again later."

            errorBody.contains("already", ignoreCase = true) ||
                    errorBody.contains("exists", ignoreCase = true) ||
                    errorBody.contains("taken", ignoreCase = true) ||
                    errorBody.contains("duplicate", ignoreCase = true) ->
                "Email or username already taken"

            errorBody.contains("password", ignoreCase = true) && code == 400 ->
                "Password is too weak"

            code == 401 || errorBody.contains("invalid", ignoreCase = true) ->
                "Wrong email or password"

            code == 403 ->
                "Account not verified. Check your email."

            else ->
                "Request failed. Please try again."
        }

        return Result.failure(Exception(userMessage))
    }
}