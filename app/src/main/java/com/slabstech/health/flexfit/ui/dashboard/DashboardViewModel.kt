package com.slabstech.health.flexfit.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val repository = GamificationRepository()

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            // Simulate real data — replace with actual API later
            _state.value = DashboardState(
                currentStreak = 12,
                todayWorkouts = 1,
                currentLevel = 15,
                totalXp = 12450,
                weeklyRank = 38,
                recentBadges = listOf("Morning Warrior", "12-Day Streak", "100 Workouts", "Level 15"),
                isLoading = false,
                userName = "Sachin"
            )
        }
    }

    fun logTodayWorkout() {
        viewModelScope.launch {
            repository.logWorkout().onSuccess {
                // Refresh dashboard after logging
                loadDashboard()
            }
        }
    }
}