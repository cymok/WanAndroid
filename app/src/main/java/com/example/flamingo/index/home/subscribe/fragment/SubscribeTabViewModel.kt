package com.example.flamingo.index.home.subscribe.fragment

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.data.DataX
import com.example.flamingo.index.common.ArticleListDataSource
import com.example.flamingo.index.common.LikeViewModel
import com.example.flamingo.network.repository.WanRepository

class SubscribeTabViewModel : LikeViewModel() {

    fun getArticlesWithPager(
        id: Int,
    ): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticleListDataSource(firstPage = 1) {
                WanRepository.getWxArticleList(id = id, page = it)
            }
        }
        return pager.liveData
    }

}