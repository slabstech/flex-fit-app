package com.slabstech.health.flexfit.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.slabstech.health.flexfit.ui.theme.FlexFitTheme

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.state.collectAsState(initial = ProfileState())

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
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF3B30)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.userName.take(2).uppercase(),
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(state.userName, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Level ${state.level} • ${state.totalXp} XP", color = Color.Gray)

        Spacer(Modifier.height(32.dp))

        // Stats Row — FIXED: weight passed correctly
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatBox(
                title = "Streak",
                value = "${state.currentStreak}",
                subtitle = "days",
                modifier = Modifier.weight(1f)   // ← CORRECT: weight on the child
            )
            StatBox(
                title = "Level",
                value = "${state.level}",
                subtitle = "Warrior",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "Rank",
                value = "#${state.weeklyRank}",
                subtitle = "Weekly",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(32.dp))

        // Recent Badges
        if (state.recentBadges.isNotEmpty()) {
            Text("Recent Badges", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.recentBadges) { badge ->
                    BadgeItem(badge)
                }
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun StatBox(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier   // ← Now receives weight correctly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(value, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF3B30))
            Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun BadgeItem(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD700)),
            contentAlignment = Alignment.Center
        ) {
            Text("Trophy", fontSize = 36.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(80.dp)
        )
    }
}