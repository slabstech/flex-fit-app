package com.slabstech.health.flexfit.network

import com.slabstech.health.flexfit.data.remote.dto.GamificationResult
import retrofit2.Response
import retrofit2.http.POST

interface ApiService {
    @POST("log-workout")
    suspend fun logWorkout(): Response<GamificationResult>
}