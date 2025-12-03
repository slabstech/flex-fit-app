package com.slabstech.health.flexfit.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WorkoutHistoryItem(
    val type: String,
    val details: String
)

class WorkoutsViewModel : ViewModel() {

    private val _workouts = MutableStateFlow<List<WorkoutHistoryItem>>(emptyList())
    val workouts: StateFlow<List<WorkoutHistoryItem>> = _workouts

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadWorkoutHistory()
    }

    private fun loadWorkoutHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            // Replace with real API later
            _workouts.value = listOf(
                WorkoutHistoryItem("Strength Training", "45 min • 350 kcal"),
                WorkoutHistoryItem("HIIT Cardio", "30 min • 420 kcal"),
                WorkoutHistoryItem("Yoga Flow", "60 min • 180 kcal")
            )
            _isLoading.value = false
        }
    }
}