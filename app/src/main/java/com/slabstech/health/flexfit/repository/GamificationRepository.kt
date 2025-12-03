// GamificationRepository.kt
package com.slabstech.health.flexfit.repository

import android.content.Context
import com.slabstech.health.flexfit.data.remote.dto.*
import com.slabstech.health.flexfit.ui.workouts.WorkoutLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GamificationRepository(private val context: Context) {

    // Create API instance with current token every time (safe & correct)
    private val api by lazy {
        RetrofitClient.getApiService(context)
    }

    suspend fun getLeaderboard(): Result<List<LeaderboardEntry>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getLeaderboard()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()} – ${response.message()}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getDashboard(): Result<UserPublic> = withContext(Dispatchers.IO) {
        try {
            val response = api.getDashboard()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Dashboard error: ${response.code()}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun logWorkout(request: WorkoutCreateRequest): Result<WorkoutResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.logWorkout(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWorkoutHistory(): Result<List<WorkoutLog>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getWorkoutHistory()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("History error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}