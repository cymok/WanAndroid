package com.example.flamingo.index.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.Banner
import com.example.flamingo.data.DataX
import com.example.flamingo.index.home.ArticlesDataSource
import com.example.flamingo.network.repository.WanRepository

class HomeViewModel : BaseViewModel() {

    val banner = MutableLiveData<Banner>()

    fun getBanner() {
        launch {
            val result = WanRepository.getBanner()
            banner.postValue(result)
        }
    }

    fun getArticlesWithPager(): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticlesDataSource(firstPage = 0, whichPage = ArticlesDataSource.HOME)
        }
        return pager.liveData
    }

}