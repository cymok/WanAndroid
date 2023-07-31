package com.example.flamingo.index.main

import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.PickData
import github.leavesczy.matisse.MediaResource

class MainViewModel : BaseViewModel() {

    val uiImage = MutableLiveData<PickData>()

    val takeUri = MutableLiveData<PickData>()
    val fileUri = MutableLiveData<PickData>()
    val pickUri = MutableLiveData<PickData>()
    val pickImages = MutableLiveData<List<MediaResource>>()

}