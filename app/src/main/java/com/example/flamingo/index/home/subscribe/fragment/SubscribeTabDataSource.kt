package com.example.flamingo.index.home.subscribe.fragment

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flamingo.data.DataX
import com.example.flamingo.network.repository.WanRepository

class SubscribeTabDataSource(
    private val id: Int = 0,
) :
    PagingSource<Int, DataX>() {

    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
        // 刷新时的页码, 返回 null 则从首页开始重新加载
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
        return try {

            val firstPage = 1

            // 当前页码
            val key = params.key ?: firstPage

            // 每页数量
            val pageSize = params.loadSize

            // 网络请求
            val result = WanRepository.getWxArticleList(id = id, page = key)

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

}