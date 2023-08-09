package com.example.flamingo.index.home.subscribe.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.DataX
import com.example.flamingo.data.LikeData
import com.example.flamingo.index.home.subscribe.fragment.paging.SubscribeTabDataSource
import com.example.flamingo.network.repository.WanRepository

class SubscribeTabViewModel : BaseViewModel() {

    fun getArticlesWithPager(
        id: Int,
    ): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            SubscribeTabDataSource(id = id)
        }
        return pager.liveData
    }

    val likeStatus = MutableLiveData<LikeData>()

    fun like(likeData: LikeData) {
        launch {
            if (likeData.like) {
                WanRepository.likeArticle(likeData.id)
                likeStatus.postValue(likeData)
            } else {
                WanRepository.unlikeArticle(likeData.id)
                likeStatus.postValue(likeData)
            }
        }
    }

}