// app/src/main/java/com/slabstech/health/flexfit/data/remote/dto/LoginResponse.kt
package com.slabstech.health.flexfit.data.remote.dto

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String = "bearer"
)