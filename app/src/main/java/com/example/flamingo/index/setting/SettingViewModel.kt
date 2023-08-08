package com.example.flamingo.index.setting

import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.network.repository.WanRepository

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
