// app/src/main/java/com/slabstech/health/flexfit/ui/dashboard/DashboardScreen.kt
package com.slabstech.health.flexfit.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.ExperimentalMaterialApi  // ← Import this
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)  // ← This suppresses the warning
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddWorkoutClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { viewModel.loadDashboard() }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 100.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Good ${getGreeting()}, ${state.userName}!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Day ${state.currentStreak} on fire!",
                        color = Color(0xFFFF3B30),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.weight(1f))
                Text("Fire", fontSize = 64.sp)
            }

            Spacer(Modifier.height(32.dp))

            // Stats Grid
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                StatBox("Level", "${state.currentLevel}", "Warrior", Modifier.weight(1f))
                StatBox("Total XP", formatNumber(state.totalXp), "points", Modifier.weight(1f))
                StatBox("Rank", "#${state.weeklyRank}", "This Week", Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                StatBox("Streak", "${state.currentStreak}", "days", Modifier.weight(1f))
                StatBox("Workouts", "${state.todayWorkouts}", "today", Modifier.weight(1f))
            }

            Spacer(Modifier.height(40.dp))

            // Placeholder for future badges
            if (state.recentBadges.isNotEmpty()) {
                Text("Recent Badges", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                // Add LazyRow later when you have badges
            }
        }

        // Pull-to-refresh spinner
        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color(0xFFFF3B30),
            contentColor = Color.White
        )

        // Error message at bottom
        state.error?.let { msg ->
            Text(
                text = msg,
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color(0xCCFF3B30), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .padding(16.dp)
            )
        }
    }
}

// ── Helper Composables ─────────────────────────────────────

@Composable
fun StatBox(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFF3B30)
            )
            Text(text = title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

private fun getGreeting(): String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 0..11 -> "Morning"
    in 12..16 -> "Afternoon"
    else -> "Evening"
}

private fun formatNumber(num: Int): String = if (num >= 1000) "${num / 1000}k" else num.toString()