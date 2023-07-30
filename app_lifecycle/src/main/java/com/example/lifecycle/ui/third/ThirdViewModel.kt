package com.example.lifecycle.ui.third

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class ThirdViewModel : ViewModel() {
    val test = liveData<String> {}
}