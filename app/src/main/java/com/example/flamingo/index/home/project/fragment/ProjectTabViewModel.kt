package com.example.flamingo.index.home.project.fragment

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.DataX
import com.example.flamingo.index.home.project.fragment.paging.ProjectTabDataSource
import com.example.flamingo.network.repository.WanRepository

class ProjectTabViewModel : BaseViewModel() {

    fun getArticlesWithPager(
        id: Int,
    ): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ProjectTabDataSource(id = id)
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