// WorkoutResponse.kt
package com.slabstech.health.flexfit.data.remote.dto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class WorkoutResponse(
    val id: Int,
    @Json(name = "created_at") val createdAt: String,
    val gamification: GamificationResult
)
