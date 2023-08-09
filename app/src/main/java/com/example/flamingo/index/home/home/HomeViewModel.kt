package com.example.flamingo.index.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.DataX
import com.example.flamingo.index.home.home.paging.HomeDataSource
import com.example.flamingo.network.repository.WanRepository

class HomeViewModel : BaseViewModel() {

    // `liveData {}` 用于页面一开始就需要获取数据的情况,
    // 这是 Coroutine, 特殊页面异常要 catch, 例如需要登录后调用的接口
    // 不 catch 的话 崩了也不知道啥情况 没有 log
    val banner = liveData {
        val result = WanRepository.getBanner()
        emit(result)
    }

    fun getArticlesWithPager(): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            HomeDataSource()
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