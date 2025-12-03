package com.slabstech.health.flexfit.repository

import com.slabstech.health.flexfit.data.remote.dto.GamificationResult
import com.slabstech.health.flexfit.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GamificationRepository {
    private val api = RetrofitClient.instance

    suspend fun logWorkout(): Result<GamificationResult> = withContext(Dispatchers.IO) {
        try {
            val response = api.logWorkout()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}