// ProfileViewModel.kt
package com.slabstech.health.flexfit.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.UserPublic
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val userName: String = "User",
    val level: Int = 1,
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val weeklyRank: Int = 999,
    val recentBadges: List<String> = emptyList(),
    val isLoading: Boolean = true
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GamificationRepository(application)  // Context passed!

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            repository.getDashboard()
                .onSuccess { user: UserPublic ->
                    _state.value = ProfileState(
                        userName = user.username,
                        level = user.level,
                        totalXp = user.xp,
                        currentStreak = user.streakCount,
                        weeklyRank = 999, // You can add real rank later via leaderboard
                        recentBadges = listOf(), // Add badge endpoint later if needed
                        isLoading = false
                    )
                }
                .onFailure {
                    // Fallback only for demo – remove in production
                    _state.value = ProfileState(
                        userName = "Sachin",
                        level = 15,
                        totalXp = 12450,
                        currentStreak = 12,
                        weeklyRank = 38,
                        recentBadges = listOf("Morning Warrior", "12-Day Streak"),
                        isLoading = false
                    )
                }
        }
    }
}