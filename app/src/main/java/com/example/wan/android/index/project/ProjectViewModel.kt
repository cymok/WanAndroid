package com.example.wan.android.index.project

import androidx.lifecycle.MutableLiveData
import com.example.wan.android.base.BaseViewModel
import com.example.wan.android.data.model.ArticlesTree
import com.example.wan.android.data.repository.WanRepository

class ProjectViewModel : BaseViewModel() {

//    val articlesTree = liveData {
////        startLoading()
//        try {
//            val result = WanRepository.getProjectTree()
//            emit(result)
//        } catch (e: Exception) {
//            loge(e)
//        }
//        stopLoading()
//    }

    val articlesTree = MutableLiveData<ArticlesTree?>()

    fun fetchArticlesTree() {
        launch(onError = {
            articlesTree.postValue(null)
        }) {
            val result = WanRepository.getProjectTree()
            articlesTree.postValue(result)
        }
    }

}