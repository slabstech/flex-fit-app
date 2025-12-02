package com.slabstech.health.flexfit.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScheduleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Flex Fit Schedule - HIIT, Zumba, Weight Training"
    }
    val text: LiveData<String> = _text
}