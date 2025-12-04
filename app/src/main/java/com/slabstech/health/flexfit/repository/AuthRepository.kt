// app/src/main/java/com/slabstech/health/flexfit/repository/AuthRepository.kt
package com.slabstech.health.flexfit.repository

import android.util.Log
import com.slabstech.health.flexfit.data.remote.ApiService
import com.slabstech.health.flexfit.data.remote.dto.LoginResponse
import com.slabstech.health.flexfit.data.remote.dto.RegisterRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import java.io.IOException

private const val TAG = "AuthRepository"

class AuthRepository(private val api: ApiService) {

    // ONE SINGLE MOSHI INSTANCE WITH KOTLIN SUPPORT — THIS FIXES EVERYTHING
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())   // ← THIS LINE WAS MISSING IN YOUR REPO
        .build()

    private val loginAdapter = moshi.adapter(LoginResponse::class.java)

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
                Result.failure(Exception(parseErrorMessage(error)))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Register exception", e)
            Result.failure(Exception("Server error. Try again later"))
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            Log.d(TAG, "Login attempt → $email")
            val response = api.login(username = email, password = password)

            if (response.isSuccessful) {
                val bodyString = response.body()?.string() ?: ""
                Log.d(TAG, "Login raw response: $bodyString")

                // NOW IT WORKS — because we added KotlinJsonAdapterFactory
                val loginResponse = loginAdapter.fromJson(bodyString)

                if (loginResponse != null && loginResponse.accessToken.isNotBlank()) {
                    Log.d(TAG, "LOGIN SUCCESS – Token (${loginResponse.accessToken.take(30)}...)")
                    return Result.success(loginResponse.accessToken)
                }
            }

            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            Log.e(TAG, "Login failed [${response.code()}]: $errorBody")
            Result.failure(Exception("Wrong email or password"))

        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            Result.failure(Exception("Check your internet connection"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected login error", e)
            Result.failure(Exception("Server error. Try again later"))
        }
    }

    private fun parseErrorMessage(errorBody: String): String = when {
        errorBody.contains("already", ignoreCase = true) -> "Email or username already taken"
        errorBody.contains("password", ignoreCase = true) -> "Password too weak"
        else -> "Registration failed"
    }
}