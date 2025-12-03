// File: app/src/main/java/com/slabstech/health/flexfit/repository/GamificationRepository.kt
package com.slabstech.health.flexfit.repository

import android.content.Context
import com.slabstech.health.flexfit.data.remote.ApiService
import com.slabstech.health.flexfit.data.remote.dto.*
import com.slabstech.health.flexfit.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GamificationRepository(private val context: Context) {

    private val api: ApiService by lazy {
        RetrofitClient.getApiService(context)
    }

    private fun getAuthHeader(): String {
        val token = TokenManager.getToken(context) ?: ""
        return "Bearer $token"
    }

    suspend fun getLeaderboard(): Result<List<LeaderboardEntry>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getLeaderboard(getAuthHeader())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Leaderboard error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDashboard(): Result<UserPublic> = withContext(Dispatchers.IO) {
        try {
            val response = api.getDashboard(getAuthHeader())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Dashboard error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logWorkout(workout: WorkoutCreateRequest): Result<WorkoutResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.logWorkout(getAuthHeader(), workout)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to log workout: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWorkoutHistory(): Result<List<WorkoutResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getWorkoutHistory(getAuthHeader())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("History error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}