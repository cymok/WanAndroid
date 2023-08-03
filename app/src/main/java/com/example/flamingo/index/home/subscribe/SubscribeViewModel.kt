package com.example.flamingo.index.home.subscribe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubscribeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is subscribe Fragment"
    }
    val text: LiveData<String> = _text
}