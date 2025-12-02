package com.slabstech.health.flexfit.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.slabstech.health.flexfit.ui.theme.FlexFitTheme

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                FlexFitTheme {
                    DashboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    fun logQuickWorkout() {
        viewModel.logTodayWorkout()
    }
}