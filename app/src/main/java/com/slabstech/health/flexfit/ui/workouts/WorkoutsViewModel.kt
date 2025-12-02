package com.slabstech.health.flexfit.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WorkoutHistoryItem(
    val type: String,
    val details: String,
    val date: String = "Today"
)

class WorkoutsViewModel : ViewModel() {
    private val repository = GamificationRepository()

    private val _workouts = MutableStateFlow<List<WorkoutHistoryItem>>(emptyList())
    val workouts: StateFlow<List<WorkoutHistoryItem>> = _workouts

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadWorkoutHistory()
    }

    fun loadWorkoutHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            // TODO: Call real /workouts/history API
            // Simulated data
            _workouts.value = listOf(
                WorkoutHistoryItem("Strength Training", "45 min • 350 kcal"),
                WorkoutHistoryItem("HIIT Cardio", "30 min • 420 kcal"),
                WorkoutHistoryItem("Zumba Dance", "60 min • 380 kcal"),
                WorkoutHistoryItem("Yoga Flow", "50 min • 180 kcal"),
                WorkoutHistoryItem("CrossFit WOD", "40 min • 500 kcal")
            )
            _isLoading.value = false
        }
    }
}