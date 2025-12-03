package com.slabstech.health.flexfit.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddWorkoutClick: () -> Unit = {}   // ← THIS LINE WAS MISSING / WRONG
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFFF3B30))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(
                    "Good ${getGreeting()}, ${state.userName}!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    "Day ${state.currentStreak} on fire!",
                    color = Color(0xFFFF3B30),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.weight(1f))
            Text("Fire", fontSize = 64.sp)
        }

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF3B30)),
            elevation = CardDefaults.cardElevation(16.dp),
            onClick = onAddWorkoutClick
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Today's Goal", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(16.dp))
                Text("${state.todayWorkouts}/3", color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.ExtraBold)
                Text("Workouts Completed", color = Color.White.copy(0.9f), fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text("Tap to log a workout!", color = Color.White.copy(0.8f), fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatBox("Level", "${state.currentLevel}", "Warrior", Modifier.weight(1f))
            StatBox("Total XP", formatNumber(state.totalXp), "points", Modifier.weight(1f))
            StatBox("Rank", "#${state.weeklyRank}", "This Week", Modifier.weight(1f))
        }

        Spacer(Modifier.height(32.dp))

        if (state.recentBadges.isNotEmpty()) {
            Text("Recent Badges", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.recentBadges) { BadgeItem(it) }
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

// Keep all your existing composables below (StatBox, BadgeItem, etc.)
@Composable
fun StatBox(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(8.dp), modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            Text(value, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF3B30))
            Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun BadgeItem(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(70.dp).clip(CircleShape).background(Color(0xFFFFD700)), contentAlignment = Alignment.Center) {
            Text("Trophy Trophy", fontSize = 36.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.width(80.dp))
    }
}

private fun getGreeting(): String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 0..11 -> "Morning"
    in 12..16 -> "Afternoon"
    else -> "Evening"
}

private fun formatNumber(num: Int): String = if (num >= 1000) "${num / 1000}k" else num.toString()