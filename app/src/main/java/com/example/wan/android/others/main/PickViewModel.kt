package com.example.wan.android.others.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wan.android.data.ImageInfo

class PickViewModel : ViewModel() {

    companion object {
        const val ERR_CANCEL = -1
        const val ERR_INVALID = -2
    }

    val imageInfo = MutableLiveData<ImageInfo>()
    val errCode = MutableLiveData<Int>()

}