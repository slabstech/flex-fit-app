// app/src/main/java/com/slabstech/health/flexfit/SplashActivity.kt
package com.slabstech.health.flexfit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slabstech.health.flexfit.ui.auth.AuthActivity
import com.slabstech.health.flexfit.utils.TokenManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BYPASS AUTH COMPLETELY → GO STRAIGHT TO MAIN APP
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        // Optional: fake a token so all API calls work immediately
        TokenManager.saveToken(this, "bypass-auth-temporary-token-123")

        finish()
    }
}