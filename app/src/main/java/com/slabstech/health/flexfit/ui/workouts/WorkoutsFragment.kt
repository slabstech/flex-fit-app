package com.slabstech.health.flexfit.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.text.SimpleDateFormat
import java.util.*

class WorkoutsFragment : Fragment() {

    private val viewModel: WorkoutsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    WorkoutHistoryScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun WorkoutHistoryScreen(viewModel: WorkoutsViewModel) {
    val workouts by viewModel.workouts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {
        Text(
            text = "Workout History",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFF3B30),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF3B30), strokeWidth = 5.dp)
            }
        } else if (workouts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No workouts yet — let's start your streak!", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                items(workouts) { workout ->
                    WorkoutHistoryCard(workout)
                }
            }
        }
    }
}

@Composable
fun WorkoutHistoryCard(workout: WorkoutLog) {
    val dateFormat = SimpleDateFormat("EEE, MMM d • h:mm a", Locale.getDefault())
    val formattedDate = try {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .parse(workout.createdAt)
            ?.let { dateFormat.format(it) } ?: "Today"
    } catch (e: Exception) {
        "Today"
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.workoutType,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = formattedDate,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(8.dp))
                Row {
                    Text("${workout.durationMin} min", color = Color(0xFFFF3B30), fontWeight = FontWeight.Medium)
                    Text("  •  ", color = Color.Gray)
                    Text("${workout.calories} kcal", color = Color(0xFFFF3B30), fontWeight = FontWeight.Medium)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                if (workout.xpEarned > 0) {
                    Text(
                        text = "+${workout.xpEarned} XP",
                        color = Color(0xFF4CAF50),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (workout.streakContinued) {
                    Text("Fire", fontSize = 36.sp)
                }
            }
        }
    }
}