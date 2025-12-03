package com.slabstech.health.flexfit.repository

import com.slabstech.health.flexfit.data.remote.dto.*
import com.slabstech.health.flexfit.network.RetrofitClient
import com.slabstech.health.flexfit.ui.workouts.WorkoutLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GamificationRepository {
    private val api = RetrofitClient.instance

    suspend fun getLeaderboard(): Result<List<LeaderboardEntry>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getLeaderboard()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
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

    suspend fun getDashboard(): Result<DashboardResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.getDashboard()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
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
                Result.failure(Exception("API Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}