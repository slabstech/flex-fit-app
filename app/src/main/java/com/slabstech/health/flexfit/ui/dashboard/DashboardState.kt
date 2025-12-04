// app/src/main/java/com/slabstech/health/flexfit/ui/dashboard/DashboardState.kt
package com.slabstech.health.flexfit.ui.dashboard

data class DashboardState(
    val currentStreak: Int = 0,
    val todayWorkouts: Int = 0,
    val currentLevel: Int = 1,
    val totalXp: Int = 0,
    val weeklyRank: Int = 999,
    val recentBadges: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val userName: String = "User",
    val error: String? = null
)