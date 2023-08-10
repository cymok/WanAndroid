package com.example.flamingo.index.article

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.DataX
import com.example.flamingo.index.common.LikeViewModel

class ArticleListViewModel : LikeViewModel() {

    fun getArticlesWithPager(
        @ArticlePage pagePath: List<String>,
        id: Int,
    ): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticleListDataSource(pagePath = pagePath, id = id)
        }
        return pager.liveData
    }

}