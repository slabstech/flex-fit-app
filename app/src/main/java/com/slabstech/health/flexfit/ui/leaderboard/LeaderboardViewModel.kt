// File: app/src/main/java/com/slabstech/health/flexfit/ui/leaderboard/LeaderboardViewModel.kt
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

    private val repository = GamificationRepository(application)

    private val _entries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val entries: StateFlow<List<LeaderboardEntry>> = _entries.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getLeaderboard()
                .onSuccess { list ->
                    _entries.value = list
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to load leaderboard"
                    _entries.value = emptyList() // No dummy data!
                }
            _isLoading.value = false
        }
    }
}