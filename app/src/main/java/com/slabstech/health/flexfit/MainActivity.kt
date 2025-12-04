// File: app/src/main/java/com/slabstech/health/flexfit/MainActivity.kt
package com.slabstech.health.flexfit

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.slabstech.health.flexfit.databinding.ActivityMainBinding
import com.slabstech.health.flexfit.ui.dashboard.DashboardFragment
import com.slabstech.health.flexfit.utils.TokenManager

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge (modern Android look)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Get NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Define top-level destinations (for drawer behavior + up button)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard,
                R.id.nav_workouts,
                R.id.nav_leaderboard,
                R.id.nav_profile,
                R.id.nav_schedule,
                R.id.nav_userplan
                // R.id.nav_home removed if you use dashboard as home
            ),
            drawerLayout
        )

        // Setup ActionBar + Navigation Drawer
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
                // Optional: Navigate to Dashboard when FAB is pressed elsewhere
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

        // Optional: Hide FAB on non-dashboard screens
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
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Optional: Handle back press to close drawer first
    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }
}