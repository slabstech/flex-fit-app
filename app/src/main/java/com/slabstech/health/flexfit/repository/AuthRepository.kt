// app/src/main/java/com/slabstech/health/flexfit/repository/AuthRepository.kt
package com.slabstech.health.flexfit.repository

import android.util.Log
import com.slabstech.health.flexfit.data.remote.ApiService
import com.slabstech.health.flexfit.data.remote.dto.RegisterRequest
import okio.IOException
import okhttp3.ResponseBody
import java.net.HttpURLConnection

private const val TAG = "AuthRepository"

class AuthRepository(private val api: ApiService) {

    suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            Log.d(TAG, "Register → $email")

            // Try with trailing slash first
            var response = api.register(RegisterRequest(username, email, password))

            // If 404, try without slash (common FastAPI issue)
            if (response.code() == 404) {
                Log.w(TAG, "404 on /register/ → trying /register")
                response = api.registerNoSlash(RegisterRequest(username, email, password))
            }

            if (response.isSuccessful) {
                val bodyString = response.body()?.string() ?: ""
                Log.d(TAG, "Register success – raw body: $bodyString")

                // Success if we get any JSON-like object or empty body
                if (bodyString.trim().isEmpty() || bodyString.trimStart().startsWith("{")) {
                    return Result.success(Unit)
                }
            }

            // === FAILURE PATH ===
            val errorBody = response.errorBody()?.string() ?: ""
            Log.e(TAG, "Register failed [${response.code()}]: $errorBody")

            val message = when {
                response.code() == HttpURLConnection.HTTP_BAD_REQUEST && errorBody.contains("already", ignoreCase = true) ->
                    "Email already registered"
                response.code() == HttpURLConnection.HTTP_BAD_REQUEST && errorBody.contains("email", ignoreCase = true) ->
                    "Invalid email format"
                response.code() == 422 -> // Validation error
                    "Please check your input"
                response.code() in 500..599 ->
                    "Server error. Try again later"
                errorBody.contains("Cloudflare", ignoreCase = true) || errorBody.contains("<html", ignoreCase = true) ->
                    "Network blocked. Try Wi-Fi or disable VPN"
                else ->
                    "Registration failed"
            }

            Result.failure(Exception(message))

        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            Result.failure(Exception("No internet connection"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error", e)
            Result.failure(Exception("Server error. Try again later"))
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            Log.d(TAG, "Login → $email")
            val response = api.login(username = email, password = password)

            if (response.isSuccessful) {
                val bodyString = response.body()?.string() ?: ""
                Log.d(TAG, "Login raw success: $bodyString")

                // Extract token safely
                val token = bodyString
                    .substringAfter("\"access_token\":\"", "")
                    .substringBefore("\"", "")
                    .takeIf { it.length > 20 }

                if (token != null) {
                    Log.d(TAG, "LOGIN SUCCESS – Token (${token.take(20)}...)")
                    return Result.success(token)
                }
            }

            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            Log.e(TAG, "Login failed [${response.code()}]: $errorBody")
            Result.failure(Exception("Wrong email or password"))

        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            Result.failure(Exception("Check your internet connection"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error", e)
            Result.failure(Exception("Server error. Try again later"))
        }
    }

    // Optional: helper to avoid duplicate error parsing
    private fun parseErrorMessage(errorBody: String, code: Int): String = when {
        errorBody.contains("already", ignoreCase = true) -> "Email already taken"
        errorBody.contains("password", ignoreCase = true) -> "Password too weak"
        code in 500..599 -> "Server error"
        else -> "Registration failed"
    }
}