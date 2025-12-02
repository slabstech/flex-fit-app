package com.slabstech.health.flexfit.data.remote

import com.slabstech.health.flexfit.data.remote.dto.WorkoutCreateRequest
import com.slabstech.health.flexfit.data.remote.dto.WorkoutResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("workouts/")
    suspend fun logWorkout(@Body request: WorkoutCreateRequest): WorkoutResponse

    companion object {
        const val BASE_URL = "https://your.gov/api"  // CHANGE TO YOUR FASTAPI URL
    }
}