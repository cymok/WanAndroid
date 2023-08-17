package com.example.wan.android.index.setting

import androidx.lifecycle.MutableLiveData
import com.example.wan.android.base.BaseViewModel
import com.example.wan.android.network.repository.WanRepository

class SettingViewModel : BaseViewModel() {

    val logStatus = MutableLiveData<Boolean>()

    fun logout() {
        launch {
            WanRepository.logout()
            onLogout()
            logStatus.postValue(false)
        }
    }

}
