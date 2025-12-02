package com.slabstech.health.flexfit.ui.leaderboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val streak: Int,
    val xp: String
)

class LeaderboardViewModel : ViewModel() {

    private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardEntry>> = _leaderboard

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        // Simulate API call
        _leaderboard.value = listOf(
            LeaderboardEntry(1, "Rajesh Singh", 98, "12,890 XP"),
            LeaderboardEntry(2, "Priya Sharma", 82, "11,200 XP"),
            LeaderboardEntry(3, "Vikram Patel", 65, "10,500 XP"),
            LeaderboardEntry(4, "Neha Gupta", 51, "9,800 XP"),
            LeaderboardEntry(5, "Arjun Mehta", 48, "9,200 XP")
        )
        _isLoading.value = false
    }
}