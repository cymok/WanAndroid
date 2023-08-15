package com.example.flamingo.index.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.data.DataX
import com.example.flamingo.index.common.ArticleListDataSource
import com.example.flamingo.index.common.LikeViewModel
import com.example.flamingo.network.repository.WanRepository

class HomeViewModel : LikeViewModel() {

    // `liveData {}` 用于页面一开始就需要获取数据的情况,
    // 这是 Coroutine, 特殊页面异常要 catch, 例如需要登录后调用的接口
    // 不 catch 的话 崩了也不知道啥情况 没有 log
    val banner = liveData {
        startLoading()
        val result = WanRepository.getBanner()
        emit(result)
    }

    fun getArticlesWithPager(): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticleListDataSource(firstPage = 0) { key ->
                // 网络请求
                val result = async { WanRepository.getHomeList(page = key) }
                // 首页加上置顶文章
                if (key == 0) {
                    val homeTopList = async { WanRepository.getHomeTopList() }
                    result.await().datas.addAll(0, homeTopList.await())
                }
                stopLoading()
                result.await()
            }
        }
        return pager.liveData
    }

}