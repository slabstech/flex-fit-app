package com.slabstech.health.flexfit.data.remote.dto

data class GymClass(
    val timeSlot: String,
    val className: String,
    val instructor: String? = null
)

data class DaySchedule(
    val day: String,
    val morning: GymClass?,
    val evening: GymClass?
)