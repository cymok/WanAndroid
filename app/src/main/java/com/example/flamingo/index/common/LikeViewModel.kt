package com.example.flamingo.index.common

import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.LikeData
import com.example.flamingo.network.repository.WanRepository

open class LikeViewModel : BaseViewModel() {

    val likeStatus = MutableLiveData<LikeData>()

    open fun like(likeData: LikeData, isMyLike: Boolean = false) {
        launch {
            if (likeData.like) {
                WanRepository.likeArticle(likeData.id)
                likeStatus.postValue(likeData)
            } else {
                if (isMyLike) {
                    WanRepository.unlikeMyLike(likeData.id, likeData.originId)
                } else {
                    WanRepository.unlikeArticle(likeData.id)
                }
                likeStatus.postValue(likeData)
            }
        }
    }

}