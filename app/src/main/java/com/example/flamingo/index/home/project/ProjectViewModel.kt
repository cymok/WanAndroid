package com.example.flamingo.index.home.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.ArticlesTree
import com.example.flamingo.data.DataX
import com.example.flamingo.index.home.ArticlesDataSource
import com.example.flamingo.network.repository.WanRepository

class ProjectViewModel : BaseViewModel() {

    val articlesTree = liveData {
        startLoading()
        val result = WanRepository.getProjectTree()
        emit(result)
        stopLoading()
    }

    fun getArticlesWithPager(id: Int): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticlesDataSource(whichPage = ArticlePage.PROJECT, id = id)
        }
        return pager.liveData
    }

}