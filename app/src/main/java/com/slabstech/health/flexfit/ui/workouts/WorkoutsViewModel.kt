package com.slabstech.health.flexfit.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WorkoutLog(
    val id: Int,
    val workoutType: String,
    val durationMin: Int,
    val calories: Int,
    val createdAt: String,
    val xpEarned: Int = 0,
    val streakContinued: Boolean = false
)

class WorkoutsViewModel : ViewModel() {

    private val repository = GamificationRepository()

    private val _workouts = MutableStateFlow<List<WorkoutLog>>(emptyList())
    val workouts: StateFlow<List<WorkoutLog>> = _workouts

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getWorkoutHistory()
                .onSuccess { list ->
                    _workouts.value = list.sortedByDescending { it.createdAt }
                }
                .onFailure {
                    // Fallback mock data
                    _workouts.value = mockData
                }
            _isLoading.value = false
        }
    }
}

private val mockData = listOf(
    WorkoutLog(1, "HIIT Cardio", 30, 420, "2025-04-05T08:30:00Z", 150, true),
    WorkoutLog(2, "Strength Training", 45, 350, "2025-04-04T18:00:00Z", 200, true),
    WorkoutLog(3, "Yoga Flow", 60, 180, "2025-04-03T07:15:00Z", 100, true),
    WorkoutLog(4, "Running", 35, 400, "2025-04-02T06:45:00Z", 180, false),
)