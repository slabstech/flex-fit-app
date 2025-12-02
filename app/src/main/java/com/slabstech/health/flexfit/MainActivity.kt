package com.slabstech.health.flexfit

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.slabstech.health.flexfit.databinding.ActivityMainBinding
import com.slabstech.health.flexfit.ui.dashboard.DashboardFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // ENHANCED FAB: Smart detection + real workout logging
        binding.appBarMain.fab.setOnClickListener { view ->
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_content_main) as? NavHostFragment
            val currentFragment = navHostFragment?.childFragmentManager?.fragments?.firstOrNull()

            when (currentFragment) {
                is DashboardFragment -> {
                    currentFragment.logQuickWorkout()
                    Snackbar.make(view, "Workout Logged! Keep the streak burning!", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(0xFFFF3B30.toInt())
                        .setTextColor(android.graphics.Color.WHITE)
                        .setAction("Undo", null)
                        .setAnchorView(binding.appBarMain.fab)
                        .show()
                }
                else -> {
                    // Keep your original behavior for other screens
                    Snackbar.make(view, "This Will Add New Workout", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(binding.appBarMain.fab)
                        .show()
                }
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // YOUR ORIGINAL NAVIGATION — 100% PRESERVED
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_schedule,
                R.id.nav_userplan,
                R.id.nav_dashboard,      // ← Add these later
                R.id.nav_workouts,
                R.id.nav_profile,
                R.id.nav_leaderboard
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}