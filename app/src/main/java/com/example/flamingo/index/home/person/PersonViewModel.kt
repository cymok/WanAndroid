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
            UserUtils.saveSupperUserInfo(result) // 获取用户信息接口返回的数据包含 UserInfo 和其它信息, 而登录注册只返回 UserInfo
            superUserInfo.postValue(result)
        }
    }

}