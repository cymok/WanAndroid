package com.example.flamingo.index.home.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is person Fragment"
    }
    val text: LiveData<String> = _text
}