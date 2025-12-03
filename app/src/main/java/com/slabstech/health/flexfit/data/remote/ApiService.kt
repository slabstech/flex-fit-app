// File: app/src/main/java/com/slabstech/health/flexfit/data/remote/ApiService.kt
package com.slabstech.health.flexfit.data.remote

import com.slabstech.health.flexfit.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // AUTH
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("register/")
    suspend fun register(@Body request: RegisterRequest): Response<UserPublic>

    // PROTECTED ENDPOINTS — ALL WITH "/"
    @GET("dashboard/")
    suspend fun getDashboard(@Header("Authorization") token: String): Response<UserPublic>

    @GET("leaderboard/")
    suspend fun getLeaderboard(@Header("Authorization") token: String): Response<List<LeaderboardEntry>>

    @GET("workouts/history/")
    suspend fun getWorkoutHistory(@Header("Authorization") token: String): Response<List<WorkoutResponse>>

    @POST("workouts/")
    suspend fun logWorkout(
        @Header("Authorization") token: String,
        @Body workout: WorkoutCreateRequest
    ): Response<WorkoutResponse>
}