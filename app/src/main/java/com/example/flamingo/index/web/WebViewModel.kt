package com.example.flamingo.index.web

import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.network.repository.WanRepository

class WebViewModel : BaseViewModel() {

    val like = MutableLiveData<Boolean>()

    fun likeArticle(id: Int) {
        launch {
            startLoading()
            WanRepository.likeArticle(id)
            like.postValue(true)
            stopLoading()
        }
    }

    fun unlikeArticle(id: Int) {
        launch {
            startLoading()
            WanRepository.unlikeArticle(id)
            like.postValue(false)
            stopLoading()
        }
    }

}