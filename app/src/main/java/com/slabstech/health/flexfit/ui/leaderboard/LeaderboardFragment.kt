package com.slabstech.health.flexfit.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.slabstech.health.flexfit.ui.theme.FlexFitTheme

class LeaderboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    LeaderboardScreen()
                }
            }
        }
    }
}

@Composable
fun LeaderboardScreen() {
    val topMembers = listOf(
        LeaderboardEntry(1, "Rajesh Singh", 98, "12,890 XP"),
        LeaderboardEntry(2, "Priya Sharma", 82, "11,200 XP"),
        LeaderboardEntry(3, "Vikram Patel", 65, "10,500 XP"),
        LeaderboardEntry(4, "Neha Gupta", 51, "9,800 XP"),
        LeaderboardEntry(5, "Arjun Mehta", 48, "9,200 XP")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
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

        LazyColumn {
            itemsIndexed(topMembers) { index, member ->
                LeaderboardRow(member, index + 1)
            }
        }
    }
}

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val streak: Int,
    val xp: String
)

@Composable
fun LeaderboardRow(entry: LeaderboardEntry, displayRank: Int) {
    val rankColor = when (displayRank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Rank
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(rankColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$displayRank",
                    color = if (displayRank <= 3) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${entry.streak} day streak • ${entry.xp}", color = Color.Gray)
            }

            Text("Star", fontSize = 32.sp)
        }
    }
}