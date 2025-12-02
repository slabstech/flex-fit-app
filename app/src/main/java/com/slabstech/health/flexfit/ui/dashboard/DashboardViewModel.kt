package com.slabstech.health.flexfit.ui.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.GamificationResult
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val repository = GamificationRepository()

    private val _state = mutableStateOf<GamificationResult?>(null)
    val state: State<GamificationResult?> = _state

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    fun logTodayWorkout() {
        viewModelScope.launch {
            _loading.value = true
            repository.logWorkout().onSuccess {
                _state.value = it
            }
            _loading.value = false
        }
    }
}