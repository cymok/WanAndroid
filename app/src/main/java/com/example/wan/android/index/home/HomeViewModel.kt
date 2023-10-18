package com.example.wan.android.index.home

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.wan.android.data.BannerItem
import com.example.wan.android.data.DataX
import com.example.wan.android.index.common.ArticleListDataSource
import com.example.wan.android.index.common.LikeViewModel
import com.example.wan.android.network.repository.WanRepository

class HomeViewModel : LikeViewModel() {

    // `liveData {}` 用于页面一开始就需要获取数据的情况,
    // 这是 Coroutine, 不 catch 的话 崩了也不知道啥情况, 控制台没有 log, 可输出到文件 或 bugly 等
//    val banner = liveData {
////        startLoading()
//        try {
//            val result = WanRepository.getBanner()
//            emit(result)
//        } catch (e: Exception) {
//            loge(e)
//        }
//    }

    val banner = MutableLiveData<List<BannerItem>>()

    fun fetchBanner() {
        launch {
//            startLoading()
            val result = WanRepository.getBanner()
            banner.postValue(result)
            stopLoading()
        }
    }

    fun getArticlesPager(): Pager<Int, DataX> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
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
            },
        )
    }

}