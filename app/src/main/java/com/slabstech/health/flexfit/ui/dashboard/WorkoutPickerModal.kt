package com.slabstech.health.flexfit.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class WorkoutType(
    val name: String,
    val durationMin: Int,
    val calories: Int,
    val emoji: String
)

val workoutOptions = listOf(
    WorkoutType("Strength Training", 45, 350, "Muscle"),
    WorkoutType("HIIT Cardio", 30, 420, "Lightning"),
    WorkoutType("Yoga Flow", 60, 180, "Lotus"),
    WorkoutType("Running", 30, 400, "Running Man"),
    WorkoutType("Quick Stretch", 15, 80, "Flexed Biceps"),
    WorkoutType("Bodyweight", 40, 320, "Push-up")
)

@Composable
fun WorkoutPickerModal(
    onDismiss: () -> Unit,
    onWorkoutSelected: (WorkoutType) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    "Log Today's Workout",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(20.dp))

                LazyColumn {
                    items(workoutOptions.size) { index ->
                        val workout = workoutOptions[index]
                        Card(
                            onClick = {
                                onWorkoutSelected(workout)
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0ED))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(workout.emoji, fontSize = 32.sp, modifier = Modifier.padding(end = 16.dp))
                                Column {
                                    Text(workout.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                    Text("${workout.durationMin} min • ${workout.calories} kcal", color = Color.Gray)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("Cancel", color = Color(0xFFFF3B30))
                }
            }
        }
    }
}