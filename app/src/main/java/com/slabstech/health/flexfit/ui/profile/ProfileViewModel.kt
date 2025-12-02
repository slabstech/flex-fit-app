package com.slabstech.health.flexfit.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.GamificationResult
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = GamificationRepository()

    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileState: StateFlow<ProfileUiState> = _profileState

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true)
            // In real app: call /profile/profile endpoint
            // For now, simulate with last workout result
            repository.logWorkout().onSuccess { result ->
                _profileState.value = ProfileUiState(
                    name = "Amit Kumar",
                    memberSince = "2024",
                    currentStreak = result.streakCount,
                    level = result.newLevel ?: 12,
                    totalXp = 8450,
                    recentBadges = result.newBadges.takeIf { it.isNotEmpty() } ?: listOf(
                        "Week Warrior", "100 Workouts", "Early Bird"
                    ),
                    isLoading = false
                )
            }
            _profileState.value = _profileState.value.copy(isLoading = false)
        }
    }
}

data class ProfileUiState(
    val name: String = "User",
    val memberSince: String = "2024",
    val currentStreak: Int = 0,
    val level: Int = 1,
    val totalXp: Int = 0,
    val recentBadges: List<String> = emptyList(),
    val isLoading: Boolean = true
)