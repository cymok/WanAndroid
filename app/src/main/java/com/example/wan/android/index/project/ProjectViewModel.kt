package com.example.wan.android.index.project

import androidx.lifecycle.liveData
import com.example.wan.android.base.BaseViewModel
import com.example.wan.android.network.repository.WanRepository

class ProjectViewModel : BaseViewModel() {

    val articlesTree = liveData {
        startLoading()
        val result = WanRepository.getProjectTree()
        emit(result)
        stopLoading()
    }

}