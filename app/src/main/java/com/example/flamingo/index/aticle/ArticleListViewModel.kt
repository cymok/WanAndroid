package com.example.flamingo.index.aticle

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.DataX
import com.example.flamingo.index.aticle.paging.ArticlesDataSource
import com.example.flamingo.network.repository.WanRepository

class ArticleListViewModel : BaseViewModel() {

    fun getArticlesWithPager(
        @ArticlePage whichPage: Int,
        id: Int,
    ): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticlesDataSource(whichPage = whichPage, id = id)
        }
        return pager.liveData
    }

    fun like(id: Int, like: Boolean) {
        launch {
            if (like) {
                WanRepository.likeArticle(id)
            } else {
                WanRepository.unlikeArticle(id)
            }
        }
    }

}