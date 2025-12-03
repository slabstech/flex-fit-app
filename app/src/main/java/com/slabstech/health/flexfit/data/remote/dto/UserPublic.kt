// File: app/src/main/java/com/slabstech/health/flexfit/data/remote/dto/UserPublic.kt
package com.slabstech.health.flexfit.data.remote.dto

import com.squareup.moshi.Json

data class UserPublic(
    val id: Int,
    val username: String,
    val email: String,
    val level: Int = 1,
    val xp: Int = 0,
    @Json(name = "streak_count") val streakCount: Int = 0,
    @Json(name = "total_workouts") val totalWorkouts: Int = 0
)