package com.example.flamingo.index.home.person

import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.SupperUserInfo
import com.example.flamingo.network.repository.WanRepository
import com.example.flamingo.utils.UserUtils

class PersonViewModel : BaseViewModel() {

    val superUserInfo = MutableLiveData<SupperUserInfo>()

    fun getUserInfo() {
        launch {
            val result = WanRepository.getUserInfo()
            val newUserInfo = result.userInfo
            UserUtils.saveUserInfo(newUserInfo)
            superUserInfo.postValue(result)
        }
    }

}