package com.example.flamingo.index.home.subscribe

import androidx.lifecycle.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.network.repository.WanRepository

class SubscribeViewModel : BaseViewModel() {

    val articlesTree = liveData {
        startLoading()
        val result = WanRepository.getWxArticleTree()
        emit(result)
        stopLoading()
    }

}