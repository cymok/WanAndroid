package com.example.wan.android.index.person

import androidx.lifecycle.MutableLiveData
import com.example.wan.android.base.BaseViewModel
import com.example.wan.android.data.model.SupperUserInfo
import com.example.wan.android.data.repository.WanRepository
import com.example.wan.android.utils.UserUtils

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