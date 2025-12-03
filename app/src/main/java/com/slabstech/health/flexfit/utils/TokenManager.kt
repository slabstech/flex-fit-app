// File: app/src/main/java/com/slabstech/health/flexfit/utils/TokenManager.kt
package com.slabstech.health.flexfit.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object TokenManager {

    private const val PREFS_NAME = "secure_prefs"
    private const val KEY_TOKEN = "jwt_token"

    private fun getMasterKeyAlias() = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private fun getEncryptedSharedPreferences(context: Context) =
        EncryptedSharedPreferences.create(
            PREFS_NAME,
            getMasterKeyAlias(),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun saveToken(context: Context, token: String) {
        val prefs = getEncryptedSharedPreferences(context)
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    fun getToken(context: Context): String? {
        val prefs = getEncryptedSharedPreferences(context)
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearToken(context: Context) {
        val prefs = getEncryptedSharedPreferences(context)
        prefs.edit()
            .remove(KEY_TOKEN)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getToken(context) != null
    }
}