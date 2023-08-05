package com.example.flamingo.index.home.square

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.network.repository.WanRepository

class SquareViewModel : BaseViewModel() {

    private val repository by lazy { WanRepository() }

    val result = MutableLiveData<String>()

/*
    val list = liveData<String> {
        emit()
    }
*/

    fun getSquareList(page: Int) {
        startLoading()
        launch {
            val result = repository.getSquareList(page)
            this.result.postValue(result)
        }
        stopLoading()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is square Fragment"
    }
    val text: LiveData<String> = _text
}