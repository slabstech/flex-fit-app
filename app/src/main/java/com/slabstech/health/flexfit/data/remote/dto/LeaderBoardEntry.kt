package com.slabstech.health.flexfit.data.remote.dto

import com.squareup.moshi.Json

data class LeaderboardEntry(
    val username: String,
    val level: Int,
    val xp: Int,
    @Json(name = "streak_count") val streakCount: Int
)