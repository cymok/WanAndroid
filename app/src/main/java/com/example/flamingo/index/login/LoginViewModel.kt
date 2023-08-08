package com.example.flamingo.index.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.data.LoginFormState
import com.example.flamingo.network.repository.WanRepository
import com.example.flamingo.utils.UserUtils

class LoginViewModel : BaseViewModel() {

    val loginFormState = MutableLiveData<LoginFormState>()

    val result = MutableLiveData<Boolean>()

    fun login(username: String, password: String) {
        launch {
            startLoading()
            val result = WanRepository.login(username, password)
            UserUtils.saveUserInfo(result)
            this.result.postValue(true)
            stopLoading()
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