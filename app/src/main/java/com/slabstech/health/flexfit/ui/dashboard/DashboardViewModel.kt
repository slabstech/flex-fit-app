package com.slabstech.health.flexfit.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.WorkoutCreateRequest
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class DashboardViewModel : ViewModel() {
    private val repository = GamificationRepository()
    private val _state = MutableStateFlow(DashboardState(isLoading = true))
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init { loadDashboard() }

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getDashboard()
                .onSuccess { data ->
                    _state.value = DashboardState(
                        currentStreak = data.currentStreak,
                        todayWorkouts = data.todayWorkouts,
                        currentLevel = data.currentLevel,
                        totalXp = data.totalXp,
                        weeklyRank = data.weeklyRank,
                        recentBadges = data.recentBadges,
                        userName = data.userName,
                        isLoading = false
                    )
                }
                .onFailure {
                    // fallback mock
                    _state.value = DashboardState(
                        currentStreak = 7, todayWorkouts = 1, currentLevel = 12,
                        totalXp = 8450, weeklyRank = 42,
                        recentBadges = listOf("7-Day Streak"), userName = "You",
                        isLoading = false
                    )
                }
        }
    }

    fun logTodayWorkout() {
        viewModelScope.launch {
            repository.logWorkout(WorkoutCreateRequest("Strength Training", 45))
                .onSuccess { loadDashboard() }
        }
    }
}