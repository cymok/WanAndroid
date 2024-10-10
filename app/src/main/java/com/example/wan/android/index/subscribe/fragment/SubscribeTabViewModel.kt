package com.example.wan.android.index.subscribe.fragment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.wan.android.data.model.DataX
import com.example.wan.android.data.repository.WanRepository
import com.example.wan.android.index.common.ArticleListDataSource
import com.example.wan.android.index.common.LikeViewModel

class SubscribeTabViewModel : LikeViewModel() {

    fun getArticlesPager(
        id: Int,
    ): Pager<Int, DataX> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                ArticleListDataSource(firstPage = 1) {
                    WanRepository.getWxArticleList(id = id, page = it)
                }
            },
        )
    }

}