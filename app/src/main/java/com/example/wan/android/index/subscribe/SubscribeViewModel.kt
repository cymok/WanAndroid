package com.example.wan.android.index.subscribe

import androidx.lifecycle.liveData
import com.example.wan.android.base.BaseViewModel
import com.example.wan.android.network.repository.WanRepository

class SubscribeViewModel : BaseViewModel() {

    val articlesTree = liveData {
        startLoading()
        val result = WanRepository.getWxArticleTree()
        emit(result)
        stopLoading()
    }

}