package com.slabstech.health.flexfit.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.state
    val loading by viewModel.loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StreakCard(streak = state?.streakCount ?: 0)

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { viewModel.logTodayWorkout() },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Log Today's Workout", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        state?.let {
            if (it.levelUp) LevelUpAnimation(it.newLevel!!)
            it.newBadges.forEach { badge -> BadgeEarnedDialog(badge) }
        }
    }
}

@Composable
fun StreakCard(streak: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF3B30)),
        elevation = CardDefaults.cardElevation(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("CURRENT STREAK", color = Color.White, fontSize = 20.sp)
            Text("$streak", fontSize = 90.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("days of fire!", color = Color.White.copy(0.9f), fontSize = 22.sp)
        }
    }
}

@Composable
fun BadgeEarnedDialog(badgeName: String) {
    var show by remember { mutableStateOf(true) }
    if (show) {
        AlertDialog(
            onDismissRequest = { show = false },
            title = { Text("Achievement Unlocked!") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("confetti.json"))
                    LottieAnimation(composition, iterations = 1, modifier = Modifier.size(150.dp))
                    Text(badgeName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                TextButton(onClick = { show = false }) { Text("Awesome!") }
            }
        )
    }
}

@Composable
fun LevelUpAnimation(newLevel: Int) {
    var show by remember { mutableStateOf(true) }
    if (show) {
        AlertDialog(
            onDismissRequest = { show = false },
            title = { Text("LEVEL UP!") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("level_up.json"))
                    LottieAnimation(composition, iterations = 1, modifier = Modifier.size(200.dp))
                    Text("Welcome to Level $newLevel!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = { TextButton(onClick = { show = false }) { Text("Let's Go!") } }
        )
    }
}