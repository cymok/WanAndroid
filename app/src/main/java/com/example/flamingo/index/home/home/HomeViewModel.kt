package com.example.flamingo.index.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.DataX
import com.example.flamingo.index.home.ArticlesDataSource
import com.example.flamingo.network.repository.WanRepository

class HomeViewModel : BaseViewModel() {

    // `liveData {}` 用于页面一开始就需要获取数据的情况
    val banner = liveData {
        val result = WanRepository.getBanner()
        emit(result)
    }

    fun getArticlesWithPager(): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticlesDataSource(whichPage = ArticlePage.HOME)
        }
        return pager.liveData
    }

}