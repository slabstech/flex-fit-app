// DashboardFragment.kt
package com.slabstech.health.flexfit.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.slabstech.health.flexfit.ui.theme.FlexFitTheme

class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    private var showWorkoutModal by mutableStateOf(false)

    // PUBLIC function called from MainActivity
    fun showWorkoutPicker() {
        showWorkoutModal = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    Box {
                        DashboardScreen(
                            viewModel = viewModel,
                            onAddWorkoutClick = { showWorkoutModal = true }
                        )

                        if (showWorkoutModal) {
                            WorkoutPickerModal(
                                onDismiss = { showWorkoutModal = false },
                                onWorkoutSelected = { workout ->
                                    viewModel.logCustomWorkout(
                                        type = workout.name,
                                        duration = workout.durationMin,
                                        calories = workout.calories
                                    )
                                    showWorkoutModal = false  // Auto-dismiss after selection
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Optional: hide modal when leaving fragment
    override fun onDestroyView() {
        super.onDestroyView()
        showWorkoutModal = false
    }
}