package com.slabstech.health.flexfit.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GamificationResult(
    @Json(name = "streak_count") val streakCount: Int,
    @Json(name = "xp_earned") val xpEarned: Int,
    @Json(name = "level_up") val levelUp: Boolean = false,
    @Json(name = "new_level") val newLevel: Int? = null,
    @Json(name = "new_badges") val newBadges: List<String> = emptyList()
)