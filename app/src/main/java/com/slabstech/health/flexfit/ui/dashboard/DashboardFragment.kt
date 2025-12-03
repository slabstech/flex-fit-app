package com.slabstech.health.flexfit.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.slabstech.health.flexfit.ui.theme.FlexFitTheme

class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    DashboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    // This method is called by MainActivity when FAB is pressed
    fun logQuickWorkout() {
        viewModel.logTodayWorkout()
    }
}