package com.example.wan.android.index.qa

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.example.wan.android.data.CommentList
import com.example.wan.android.index.common.ArticleWebViewModel
import com.example.wan.android.network.repository.WanRepository

class QaWebViewModel : ArticleWebViewModel() {

    val commentList = MutableLiveData<CommentList>()

    fun getQACommentList(id: Int) {
        launch {
            val result = WanRepository.getQACommentList(id)
            LogUtils.e(result)
            commentList.postValue(result)
        }
    }

}