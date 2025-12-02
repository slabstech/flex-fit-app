package com.slabstech.health.flexfit.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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

class WorkoutsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    WorkoutsScreen()
                }
            }
        }
    }
}

@Composable
fun WorkoutsScreen() {
    val workoutHistory = listOf(
        "Strength Training" to "45 min • 350 kcal",
        "HIIT Cardio" to "30 min • 420 kcal",
        "Zumba Dance" to "60 min • 380 kcal",
        "Yoga Flow" to "50 min • 180 kcal",
        "CrossFit WOD" to "40 min • 500 kcal"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Workout History",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF3B30)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(workoutHistory) { (type, details) ->
                WorkoutCard(type = type, details = details)
            }
        }
    }
}

@Composable
fun WorkoutCard(type: String, details: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3F0)),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(type, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(details, color = Color.Gray, fontSize = 14.sp)
            }
            Icon(
                imageVector = Icons.Default.LocationOn,   // ← Now resolved!
                contentDescription = "Workout",
                tint = Color(0xFFFF3B30),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}