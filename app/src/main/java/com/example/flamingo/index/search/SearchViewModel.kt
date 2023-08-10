package com.example.flamingo.index.search

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.data.DataX
import com.example.flamingo.index.common.ArticleListDataSource
import com.example.flamingo.index.common.LikeViewModel
import com.example.flamingo.network.repository.WanRepository

class SearchViewModel : LikeViewModel() {

    fun search(key: String): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticleListDataSource(firstPage = 0) {
                WanRepository.search(page = it, key = key)
            }
        }
        return pager.liveData
    }

}