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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue   // ← THIS WAS MISSING
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
    val entries by viewModel.leaderboard.collectAsState()     // ← Works inside @Composable
    val isLoading by viewModel.isLoading.collectAsState()     // ← Works inside @Composable

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
                    LeaderboardRow(entry, index + 1)
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(entry: LeaderboardEntry, rank: Int) {
    val rankColor = when (rank) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> Color(0xFF888888)
    }

    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(rankColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$rank",
                    color = if (rank <= 3) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${entry.streak} day streak • ${entry.xp}", color = Color.Gray)
            }
            Text("Star", fontSize = 32.sp)
        }
    }
}