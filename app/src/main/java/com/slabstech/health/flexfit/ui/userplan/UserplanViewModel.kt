package com.slabstech.health.flexfit.ui.userplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserplanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "User Plan - Curated"
    }
    val text: LiveData<String> = _text
}