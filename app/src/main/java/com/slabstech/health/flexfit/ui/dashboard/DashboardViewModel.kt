// DashboardViewModel.kt
package com.slabstech.health.flexfit.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.UserPublic
import com.slabstech.health.flexfit.data.remote.dto.WorkoutCreateRequest
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GamificationRepository(application)  // ← Context passed!

    private val _state = MutableStateFlow(DashboardState(isLoading = true))
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init { loadDashboard() }

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            repository.getDashboard()
                .onSuccess { user ->
                    _state.value = DashboardState(
                        currentStreak = user.streakCount,
                        todayWorkouts = 1, // You can improve this later with real count
                        currentLevel = user.level,
                        totalXp = user.xp,
                        weeklyRank = 999, // Will come from leaderboard later
                        recentBadges = listOf(), // Optional: fetch from /badges later
                        userName = user.username,
                        isLoading = false
                    )
                }
                .onFailure {
                    // Fallback only for demo — remove later
                    _state.value = DashboardState(
                        currentStreak = 7,
                        todayWorkouts = 1,
                        currentLevel = 12,
                        totalXp = 8450,
                        weeklyRank = 42,
                        recentBadges = listOf("7-Day Streak"),
                        userName = "FlexHero",
                        isLoading = false
                    )
                }
        }
    }

    fun logCustomWorkout(type: String, duration: Int, calories: Int?) {
        viewModelScope.launch {
            val request = WorkoutCreateRequest(
                workoutType = type,      // ← Correct property name
                durationMin = duration,  // ← Correct property name
                calories = calories
            )
            repository.logWorkout(request)
                .onSuccess { loadDashboard() }
                .onFailure { throwable ->
                    // Optional: show error toast
                    println("Workout log failed: $throwable")
                }
        }
    }
}