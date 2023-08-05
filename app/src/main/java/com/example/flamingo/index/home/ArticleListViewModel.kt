package com.example.flamingo.index.home

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.DataX

class ArticleListViewModel : BaseViewModel() {

    fun getArticlesWithPager(
        @ArticlesDataSource.Page whichPage: Int,
        id: Int,
    ): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticlesDataSource(firstPage = 1, whichPage = whichPage, id = id)
        }
        return pager.liveData
    }

}