package com.slabstech.health.flexfit.data.remote.dto

import com.squareup.moshi.Json

data class DashboardResponse(
    @Json(name = "current_streak") val currentStreak: Int,
    @Json(name = "today_workouts") val todayWorkouts: Int,
    @Json(name = "current_level") val currentLevel: Int,
    @Json(name = "total_xp") val totalXp: Int,
    @Json(name = "weekly_rank") val weeklyRank: Int,
    @Json(name = "recent_badges") val recentBadges: List<String>,
    @Json(name = "user_name") val userName: String
)