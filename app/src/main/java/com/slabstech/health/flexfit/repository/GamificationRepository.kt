package com.slabstech.health.flexfit.repository

import com.slabstech.health.flexfit.data.remote.dto.GamificationResult
import com.slabstech.health.flexfit.network.RetrofitClient

class GamificationRepository {
    private val api = RetrofitClient.api

    suspend fun logWorkout(
        type: String = "strength",
        durationMin: Int = 45,
        calories: Int? = 350
    ): Result<GamificationResult> {
        return try {
            val response = api.logWorkout(
                com.slabstech.health.flexfit.data.remote.dto.WorkoutCreateRequest(type, durationMin, calories)
            )
            Result.success(response.gamification)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}