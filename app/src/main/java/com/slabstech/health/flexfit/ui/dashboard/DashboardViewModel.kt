// app/src/main/java/com/slabstech/health/flexfit/ui/dashboard/DashboardViewModel.kt
package com.slabstech.health.flexfit.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.WorkoutCreateRequest
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GamificationRepository(application.applicationContext)

    private val _state = MutableStateFlow(DashboardState(isLoading = true))
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            repository.getDashboard()
                .onSuccess { user ->
                    _state.value = DashboardState(
                        userName = user.username.replaceFirstChar { it.uppercaseChar() },
                        currentStreak = user.streakCount,
                        todayWorkouts = user.totalWorkouts, // Real count from backend!
                        currentLevel = user.level,
                        totalXp = user.xp,
                        weeklyRank = 999, // TODO: later from leaderboard
                        recentBadges = emptyList(), // TODO: badges later
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _state.value = DashboardState(
                        userName = "User",
                        currentStreak = 0,
                        todayWorkouts = 0,
                        currentLevel = 1,
                        totalXp = 0,
                        weeklyRank = 999,
                        recentBadges = emptyList(),
                        isLoading = false,
                        error = "Failed to load profile. Pull to refresh."
                    )
                }
        }
    }

    fun logCustomWorkout(type: String, duration: Int, calories: Int?) {
        viewModelScope.launch {
            val request = WorkoutCreateRequest(
                workoutType = type,
                durationMin = duration,
                calories = calories
            )

            repository.logWorkout(request)
                .onSuccess {
                    loadDashboard() // Refresh with new XP, streak, workout count
                }
                .onFailure {
                    // You can emit error here later
                }
        }
    }
}