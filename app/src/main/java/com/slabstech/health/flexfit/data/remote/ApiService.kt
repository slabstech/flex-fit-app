package com.slabstech.health.flexfit.network

import com.slabstech.health.flexfit.data.remote.dto.*
import com.slabstech.health.flexfit.ui.workouts.WorkoutLog
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("leaderboard")
    suspend fun getLeaderboard(): Response<List<LeaderboardEntry>>

    @GET("dashboard")
    suspend fun getDashboard(): Response<UserPublic>  // ← new!

    @POST("workouts")
    suspend fun logWorkout(@Body request: WorkoutCreateRequest): Response<WorkoutResponse>

    @GET("workouts/history")
    suspend fun getWorkoutHistory(): Response<List<WorkoutLog>>
}