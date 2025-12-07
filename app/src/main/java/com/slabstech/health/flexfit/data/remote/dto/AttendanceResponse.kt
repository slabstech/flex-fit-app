package com.slabstech.health.flexfit.data.remote.dto

// AttendanceResponse.kt
data class AttendanceResponse(
    val message: String,
    val student_name: String? = null,
    val timestamp: String? = null
)