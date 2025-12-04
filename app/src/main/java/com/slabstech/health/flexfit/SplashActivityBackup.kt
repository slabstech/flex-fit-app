// File: app/src/main/java/com/slabstech/health/flexfit/SplashActivity.kt
package com.slabstech.health.flexfit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slabstech.health.flexfit.ui.auth.AuthActivity
import com.slabstech.health.flexfit.utils.TokenManager

@SuppressLint("CustomSplashScreen")
class SplashActivityBackup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
        val intent = if (TokenManager.getToken(this) != null) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java)
        }

        startActivity(intent)
        finish() // Prevent going back to splash
    }
}