// app/src/main/java/com/slabstech/health/flexfit/data/remote/ApiService.kt
package com.slabstech.health.flexfit.data.remote

import com.slabstech.health.flexfit.data.remote.dto.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // LOGIN – already perfect
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<ResponseBody>

    // REGISTER – NOW SAFE (same pattern as login)
    @POST("register/")
    suspend fun register(@Body request: RegisterRequest): Response<ResponseBody>

    // Optional: also support without trailing slash (prevents 404 → HTML crash)
    @POST("register")
    suspend fun registerNoSlash(@Body request: RegisterRequest): Response<ResponseBody>

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