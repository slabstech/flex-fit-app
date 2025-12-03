package com.slabstech.health.flexfit.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.LeaderboardEntry
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel : ViewModel() {
    private val repository = GamificationRepository()

    private val _entries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val entries: StateFlow<List<LeaderboardEntry>> = _entries.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getLeaderboard()
                .onSuccess { _entries.value = it }
                .onFailure {
                    // fallback
                    _entries.value = listOf(
                        LeaderboardEntry("gymbro", 1, 0, 0),
                        LeaderboardEntry("flex-11", 1, 0, 0)
                    )
                }
            _isLoading.value = false
        }
    }
}