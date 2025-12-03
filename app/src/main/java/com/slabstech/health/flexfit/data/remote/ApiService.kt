package com.slabstech.health.flexfit.network

import com.slabstech.health.flexfit.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("leaderboard/")
    suspend fun getLeaderboard(): Response<List<LeaderboardEntry>>

    @POST("workouts/")
    suspend fun logWorkout(@Body request: WorkoutCreateRequest): Response<WorkoutResponse>

    @GET("dashboard/")
    suspend fun getDashboard(): Response<DashboardResponse>
}