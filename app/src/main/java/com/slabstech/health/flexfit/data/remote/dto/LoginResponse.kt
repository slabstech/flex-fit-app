// File: app/src/main/java/com/slabstech/health/flexfit/data/remote/dto/LoginResponse.kt
package com.slabstech.health.flexfit.data.remote.dto

data class LoginResponse(
    val access_token: String,
    val token_type: String = "bearer"
)