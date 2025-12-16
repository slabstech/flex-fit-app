package com.slabstech.health.flexfit.ui.events


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.slabstech.health.flexfit.R
import com.slabstech.health.flexfit.data.remote.dto.GymScheduleData

class GymScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gym_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<LinearLayout>(R.id.scheduleContainer)

        GymScheduleData.december2025Schedule.forEach { daySchedule ->
            val row = layoutInflater.inflate(R.layout.item_schedule_row, container, false) as LinearLayout

            val tvDay = row.findViewById<TextView>(R.id.tvDay)
            val tvMorning = row.findViewById<TextView>(R.id.tvMorning)
            val tvEvening = row.findViewById<TextView>(R.id.tvEvening)

            tvDay.text = daySchedule.day

            if (daySchedule.morning != null) {
                tvMorning.text = "${daySchedule.morning.className}\n(${daySchedule.morning.instructor})".trim()
            } else {
                tvMorning.text = "-"
                tvMorning.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
            }

            if (daySchedule.evening != null) {
                tvEvening.text = "${daySchedule.evening.timeSlot}\n${daySchedule.evening.className}\n(${daySchedule.evening.instructor})".trim()
            } else if (daySchedule.day == "Saturday") {
                // Special case already handled below
                tvEvening.text = "See note below"
            } else {
                tvEvening.text = "Closed"
                tvEvening.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
            }

            container.addView(row)
        }
    }
}