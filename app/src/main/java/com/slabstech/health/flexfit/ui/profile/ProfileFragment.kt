package com.slabstech.health.flexfit.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.slabstech.health.flexfit.ui.theme.FlexFitTheme

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile picture circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF3B30)),          // ← Fixed
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AK",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Amit Kumar", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Member since 2024", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        // Stats grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard("Streak", "28", "days")
            StatCard("Level", "12", "Warrior")
            StatCard("Total XP", "8,450", "points")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Recent Badges", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BadgeItem("Week Warrior")
            BadgeItem("100 Workouts")
            BadgeItem("Early Bird")
        }
    }
}

@Composable
fun StatCard(title: String, value: String, subtitle: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0ED)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(value, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF3B30))
            Text(title, fontWeight = FontWeight.Medium)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun BadgeItem(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("Star", fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}