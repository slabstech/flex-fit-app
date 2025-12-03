package com.slabstech.health.flexfit.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class ProfileViewModel : ViewModel() {
    private val repository = GamificationRepository()

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Use getDashboard() or create a dedicated getProfile() endpoint
            repository.getDashboard()
                .onSuccess { data ->
                    _state.value = ProfileState(
                        userName = data.userName,
                        level = data.currentLevel,
                        totalXp = data.totalXp,
                        currentStreak = data.currentStreak,
                        weeklyRank = data.weeklyRank,
                        recentBadges = data.recentBadges,
                        isLoading = false
                    )
                }
                .onFailure {
                    // Fallback mock data
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