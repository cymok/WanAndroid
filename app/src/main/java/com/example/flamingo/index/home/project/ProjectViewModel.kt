package com.example.flamingo.index.home.project

import androidx.lifecycle.liveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.network.repository.WanRepository

class ProjectViewModel : BaseViewModel() {

    val articlesTree = liveData {
        startLoading()
        val result = WanRepository.getProjectTree()
        emit(result)
        stopLoading()
    }

}