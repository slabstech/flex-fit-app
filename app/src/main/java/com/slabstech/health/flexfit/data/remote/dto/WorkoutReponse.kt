// data/remote/dto/WorkoutResponse.kt
package com.slabstech.health.flexfit.data.remote.dto

import com.squareup.moshi.Json

data class WorkoutResponse(
    val id: Int,
    @Json(name = "workout_type") val workoutType: String,
    @Json(name = "duration_min") val durationMin: Int,
    val calories: Int?,
    @Json(name = "created_at") val createdAt: String

)

