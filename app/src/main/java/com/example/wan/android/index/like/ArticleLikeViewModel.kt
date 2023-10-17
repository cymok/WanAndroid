package com.example.wan.android.index.like

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.wan.android.data.DataX
import com.example.wan.android.index.common.ArticleListDataSource
import com.example.wan.android.index.common.LikeViewModel
import com.example.wan.android.network.repository.WanRepository

class ArticleLikeViewModel : LikeViewModel() {

    fun getArticlesPager(): Pager<Int, DataX> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                ArticleListDataSource(firstPage = 0) {
                    WanRepository.getLikeList(page = it).apply {
                        datas.forEach { data ->
                            // 收藏列表全是已收藏 接口没有返回
                            data.collect = true
                        }
                    }
                }
            },
        )
    }

}