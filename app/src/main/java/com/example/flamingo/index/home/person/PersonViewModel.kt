package com.example.flamingo.index.home.person

import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.SupperUserInfo
import com.example.flamingo.network.repository.WanRepository

class PersonViewModel : BaseViewModel() {

    val userInfo = MutableLiveData<SupperUserInfo>()

    fun getUserInfo() {
        launch {
            val result = WanRepository.getUserInfo()
            userInfo.postValue(result)
        }
    }

}