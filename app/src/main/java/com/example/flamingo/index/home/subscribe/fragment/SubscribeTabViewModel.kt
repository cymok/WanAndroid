package com.example.flamingo.index.home.subscribe.fragment

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.flamingo.data.DataX
import com.example.flamingo.index.common.LikeViewModel

class SubscribeTabViewModel : LikeViewModel() {

    fun getArticlesWithPager(
        id: Int,
    ): LiveData<PagingData<DataX>> {
        val pager = Pager(PagingConfig(pageSize = 10)) {
            SubscribeTabDataSource(id = id)
        }
        return pager.liveData
    }

}