package com.example.flamingo.index.web

import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.network.repository.WanRepository

open class WebViewModel : BaseViewModel() {

    val like = MutableLiveData<Boolean>()

    fun likeArticle(id: Int, originId: Int, isMyLike: Boolean) {
        launch {
            startLoading()
            if (isMyLike) {
                WanRepository.likeArticle(originId)
            } else {
                WanRepository.likeArticle(id)
            }
            like.postValue(true)
            stopLoading()
        }
    }

    fun unlikeArticle(id: Int, originId: Int, isMyLike: Boolean) {
        launch {
            startLoading()
            if (isMyLike) {
                WanRepository.unlikeMyLike(id, originId)
            } else {
                WanRepository.unlikeArticle(id)
            }
            like.postValue(false)
            stopLoading()
        }
    }

}