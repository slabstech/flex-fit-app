package com.slabstech.health.flexfit.data.remote.dto

object GymScheduleData {
    val december2025Schedule = listOf(
        DaySchedule(
            day = "Monday",
            morning = GymClass("8am to 9am", "Yoga", "Mamatha"),
            evening = GymClass("6:30pm to 9:30pm", "HIIT / Functional Training", "")
        ),
        DaySchedule(
            day = "Tuesday",
            morning = GymClass("8am to 9am", "Zumba", "Pavithra"),
            evening = GymClass("6:30pm to 9:30pm", "Body Pump", "Girish / Kamath")
        ),
        DaySchedule(
            day = "Wednesday",
            morning = GymClass("8am to 9am", "Yoga", "Mamatha"),
            evening = GymClass("6:30pm to 9:30pm", "Pilates", "Pavithra")
        ),
        DaySchedule(
            day = "Thursday",
            morning = GymClass("8am to 9am", "Pilates", "Pavithra"),
            evening = GymClass("6:30pm to 9:30pm", "Yoga", "Mamatha")
        ),
        DaySchedule(
            day = "Friday",
            morning = GymClass("8am to 9am", "HIIT / Functional Training", "Gowda / Girish"),
            evening = GymClass("6:30pm to 9:30pm", "Zumba", "Pavithra")
        ),
        DaySchedule(
            day = "Saturday",
            morning = null,
            evening = GymClass("8:30am to 9:30am", "Zumba", "")
        ),
        DaySchedule(
            day = "Sunday",
            morning = null,
            evening = null
        )
    )
}