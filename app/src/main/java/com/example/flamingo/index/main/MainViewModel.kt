package com.example.flamingo.index.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.flamingo.data.PickData
import github.leavesczy.matisse.MediaResource

class MainViewModel(app: Application) : AndroidViewModel(app) {

    val uiImage = MutableLiveData<PickData>()

    val takeUri = MutableLiveData<PickData>()
    val fileUri = MutableLiveData<PickData>()
    val pickUri = MutableLiveData<PickData>()
    val pickImages = MutableLiveData<List<MediaResource>>()

}