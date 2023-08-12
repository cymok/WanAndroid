package com.example.flamingo.index.qa

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.data.CommentList
import com.example.flamingo.index.web.WebViewModel
import com.example.flamingo.network.repository.WanRepository

class QaWebViewModel : WebViewModel() {

    val commentList = MutableLiveData<CommentList>()

    fun getQACommentList(id: Int) {
        launch {
            val result = WanRepository.getQACommentList(id)
            LogUtils.e(result)
            commentList.postValue(result)
        }
    }

}