// WorkoutsViewModel.kt
package com.slabstech.health.flexfit.ui.workouts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.WorkoutResponse
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkoutLog(
    val id: Int,
    val workoutType: String,
    val durationMin: Int,
    val calories: Int?,
    val createdAt: String,
    val xpEarned: Int = 0,
    val streakContinued: Boolean = false
)

class WorkoutsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GamificationRepository(application)

    private val _workouts = MutableStateFlow<List<WorkoutLog>>(emptyList())
    val workouts: StateFlow<List<WorkoutLog>> = _workouts.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init { loadHistory() }

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getWorkoutHistory()
                .onSuccess { workoutResponses ->
                    val logs = workoutResponses.map { workout ->
                        WorkoutLog(
                            id = workout.id,
                            workoutType = workout.workoutType,
                            durationMin = workout.durationMin,
                            calories = workout.calories,
                            createdAt = workout.createdAt,
                            xpEarned = 0,                    // History doesn't return XP — normal
                            streakContinued = false          // Not available in history
                        )
                    }.sortedByDescending { it.createdAt }

                    _workouts.value = logs
                }
                .onFailure {
                    _workouts.value = mockData
                }
            _isLoading.value = false
        }
    }
}

private val mockData = listOf(
    WorkoutLog(1, "HIIT Cardio", 30, 420, "2025-04-05T08:30:00Z", 180, true),
    WorkoutLog(2, "Strength Training", 45, 350, "2025-04-04T18:00:00Z", 200, true),
    WorkoutLog(3, "Yoga Flow", 60, 180, "2025-04-03T07:15:00Z", 100, true),
)