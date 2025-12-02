package com.slabstech.health.flexfit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Flex Fit - Level up Life"
    }
    val text: LiveData<String> = _text
}