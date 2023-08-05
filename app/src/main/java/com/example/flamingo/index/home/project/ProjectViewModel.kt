package com.example.flamingo.index.home.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.DataX
import com.example.flamingo.data.ArticlesTree
import com.example.flamingo.index.home.ArticlesDataSource
import com.example.flamingo.network.repository.WanRepository

class ProjectViewModel : BaseViewModel() {

    val articlesTree = MutableLiveData<ArticlesTree>()

    fun getProjectTree() {
        launch {
            startLoading()
            val result = WanRepository.getProjectTree()
            articlesTree.postValue(result)
            stopLoading()
        }
    }

    fun getArticlesWithPager(id: Int): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            ArticlesDataSource(firstPage = 1, whichPage = ArticlesDataSource.PROJECT, id = id)
        }
        return pager.liveData
    }

}