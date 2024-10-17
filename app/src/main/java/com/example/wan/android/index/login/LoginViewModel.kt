package com.example.wan.android.index.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.example.wan.android.base.BaseViewModel
import com.example.wan.android.constant.AppConst
import com.example.wan.android.data.model.LoginFormState
import com.example.wan.android.data.model.SupperUserInfo
import com.example.wan.android.data.repository.WanRepository
import com.example.wan.android.utils.UserUtils

class LoginViewModel : BaseViewModel() {

    val loginFormState = MutableLiveData<LoginFormState>()

    val result = MutableLiveData<Boolean>()

    fun login(username: String, password: String) {
        launch {
            changeLoadingState(AppConst.loading)
            val result = WanRepository.login(username, password)
            // 保存用户信息
            // 获取用户信息接口返回的数据包含 UserInfo 和其它信息, 而登录注册只返回 UserInfo
            UserUtils.saveSupperUserInfo(
                SupperUserInfo(
                    userInfo = result,
                    coinInfo = null,
                    collectArticleInfo = null,
                )
            )
            this.result.postValue(true)
            changeLoadingState(AppConst.complete)
        }
    }

    fun register(username: String, password: String) {
        launch {
            changeLoadingState(AppConst.loading)
            val result = WanRepository.register(username, password)
            UserUtils.saveSupperUserInfo(
                SupperUserInfo(
                    userInfo = result,
                    coinInfo = null,
                    collectArticleInfo = null,
                )
            )
            this.result.postValue(true)
            changeLoadingState(AppConst.complete)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            loginFormState.value = LoginFormState(usernameError = "用户名格式有误")
        } else if (!isPasswordValid(password)) {
            loginFormState.value = LoginFormState(passwordError = "密码格式有误")
        } else {
            loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    // 用户名校验
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // 密码校验
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}