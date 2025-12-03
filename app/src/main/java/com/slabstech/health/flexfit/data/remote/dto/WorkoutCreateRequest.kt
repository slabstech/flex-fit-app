package com.slabstech.health.flexfit.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkoutCreateRequest(
    @Json(name = "workout_type") val workoutType: String,
    @Json(name = "duration_min") val durationMin: Int,
    val calories: Int? = null
)