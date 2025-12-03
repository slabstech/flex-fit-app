package com.slabstech.health.flexfit.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape   // ← Correct import
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.slabstech.health.flexfit.ui.theme.FlexFitTheme

class LeaderboardFragment : Fragment() {

    private val viewModel: LeaderboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    LeaderboardScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel) {
    val entries by viewModel.entries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF3B30))
                .padding(24.dp)
        ) {
            Text(
                "Weekly Leaderboard",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF3B30))
            }
        } else {
            LazyColumn {
                itemsIndexed(entries) { index, entry ->
                    LeaderboardRow(
                        rank = index + 1,
                        username = entry.username,
                        level = entry.level,
                        xp = entry.xp,
                        streak = entry.streakCount
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(rank: Int, username: String, level: Int, xp: Int, streak: Int) {
    val rankColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color(0xFF888888)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(rankColor, CircleShape),   // ← FIXED: CircleShape
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    color = if (rank <= 3) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(username, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "Level $level • $xp XP • $streak day streak",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Text("Trophy", fontSize = 32.sp)
        }
    }
}