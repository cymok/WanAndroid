package com.example.wan.android.index.project.fragment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.wan.android.data.DataX
import com.example.wan.android.index.common.ArticleListDataSource
import com.example.wan.android.index.common.LikeViewModel
import com.example.wan.android.network.repository.WanRepository

class ProjectTabViewModel : LikeViewModel() {

    fun getArticlesPager(
        id: Int?,
    ): Pager<Int, DataX> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                if (id == null) { // 前面手动加在最前面的null 最新项目
                    ArticleListDataSource(firstPage = 0) {
                        WanRepository.getNewProjectList(page = it)
                    }
                } else {
                    ArticleListDataSource(firstPage = 1) {
                        WanRepository.getProjectList(id = id, page = it)
                    }
                }
            },
        )
    }

}