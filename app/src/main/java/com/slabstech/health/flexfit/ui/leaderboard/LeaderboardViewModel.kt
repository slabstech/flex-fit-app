// LeaderboardViewModel.kt
package com.slabstech.health.flexfit.ui.leaderboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.slabstech.health.flexfit.data.remote.dto.LeaderboardEntry
import com.slabstech.health.flexfit.repository.GamificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GamificationRepository(application)  // Context passed!

    private val _entries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val entries: StateFlow<List<LeaderboardEntry>> = _entries.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getLeaderboard()
                .onSuccess { list ->
                    _entries.value = list
                }
                .onFailure {
                    // Optional: remove fallback in production
                    _entries.value = listOf(
                        LeaderboardEntry("gymbro", 12, 8450, 42),
                        LeaderboardEntry("flex-11", 10, 7200, 28),
                        LeaderboardEntry("powerlifter", 9, 6800, 15)
                    )
                }
            _isLoading.value = false
        }
    }
}