package com.example.wan.android.index.project

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.wan.android.data.DataX
import com.example.wan.android.index.common.ArticleListDataSource
import com.example.wan.android.index.common.LikeViewModel
import com.example.wan.android.network.repository.WanRepository

class NewProjectViewModel : LikeViewModel() {

    fun getArticlesPager(): Pager<Int, DataX> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                ArticleListDataSource(firstPage = 0) {
                    WanRepository.getNewProjectList(page = it)
                }
            },
        )
    }

}