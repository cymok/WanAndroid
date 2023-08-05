package com.example.flamingo.index.home

import androidx.annotation.IntDef
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flamingo.data.DataX
import com.example.flamingo.network.repository.WanRepository

// 根据接口情况 有的是0 有的是1
class ArticlesDataSource(
    private val firstPage: Int,
    @Page private val whichPage: Int,
    private val id: Int = 0,
) :
    PagingSource<Int, DataX>() {

    companion object {
        const val HOME = 0
        const val PROJECT = 1
        const val SQUARE = 2
        const val SUBSCRIBE = 3
    }

    @Retention(AnnotationRetention.RUNTIME)
    @IntDef(*[HOME, PROJECT, SQUARE, SUBSCRIBE])
    annotation class Page

    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
        // 刷新时的页码, 返回 null 则从首页开始重新加载
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
        return try {
            // 当前页码
            val key = params.key ?: firstPage

            // 每页数量
            val pageSize = params.loadSize

            // 网络请求
            val result = requestList(key)

            // 前一页 页码
            val preKey = if (key > firstPage) {
                key - 1
            } else {
                null
            }

            // 下一页 页码
            // 判断是否还有下一页 有很多方式
            val nextKey = if (result.over) {
                null
            } else {
                key + 1
            }

            LoadResult.Page(result.datas, preKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun requestList(page: Int) = when (whichPage) {
        HOME -> {
            WanRepository.getHomeList(page = page)
        }

        PROJECT -> {
            WanRepository.getProjectList(id = id, page = page)
        }

        SQUARE -> {
            WanRepository.getSquareList(page = page)
        }

        SUBSCRIBE -> {
            WanRepository.getWxArticleList(id = id, page = page)
        }

        else -> {
            WanRepository.getHomeList(page = page)
        }
    }

}