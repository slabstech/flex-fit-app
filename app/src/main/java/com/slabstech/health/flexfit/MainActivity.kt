package com.slabstech.health.flexfit

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.slabstech.health.flexfit.databinding.ActivityMainBinding
import com.slabstech.health.flexfit.ui.dashboard.DashboardFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Get NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Top-level destinations (no Up button on these)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard,
                R.id.nav_workouts,
                R.id.nav_schedule,
                R.id.nav_leaderboard,
                R.id.nav_profile
            )
        )

        // Setup Toolbar
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Setup Bottom Navigation
        val bottomNav: BottomNavigationView = binding.bottomNavView // Add this ID to the view in XML
        bottomNav.setupWithNavController(navController)

        // FAB — Only active on Dashboard
        binding.appBarMain.fab.setOnClickListener { view ->
            val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull()

            if (currentFragment is DashboardFragment) {
                currentFragment.showWorkoutPicker()

                Snackbar.make(view, "Choose your workout!", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(0xFFFF3B30.toInt())
                    .setTextColor(Color.WHITE)
                    .setAnchorView(binding.appBarMain.fab)
                    .show()
            } else {
                // Navigate to Dashboard if elsewhere
                if (navController.currentDestination?.id != R.id.nav_dashboard) {
                    navController.navigate(R.id.nav_dashboard)
                }
                Snackbar.make(view, "Go to Dashboard to log a workout", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xFF333333.toInt())
                    .setTextColor(Color.WHITE)
                    .setAnchorView(binding.appBarMain.fab)
                    .show()
            }
        }

        // Show/hide FAB based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_dashboard -> binding.appBarMain.fab.show()
                else -> binding.appBarMain.fab.hide()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}