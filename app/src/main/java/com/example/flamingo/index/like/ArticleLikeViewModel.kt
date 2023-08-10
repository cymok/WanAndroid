package com.example.flamingo.index.like

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.data.DataX
import com.example.flamingo.index.common.ArticleListDataSource
import com.example.flamingo.index.common.LikeViewModel
import com.example.flamingo.network.repository.WanRepository

class ArticleLikeViewModel: LikeViewModel() {

    fun getArticlesWithPager(): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticleListDataSource(firstPage = 0) {
                WanRepository.getLikeList(page = it).apply {
                    datas.forEach { data ->
                        // 收藏列表全是已收藏 接口没有返回
                        data.collect = true
                    }
                }
            }
        }
        return pager.liveData
    }

}