package com.example.flamingo.base.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.constant.AppConst
import com.example.flamingo.index.login.LoginActivity
import com.example.flamingo.utils.toast

abstract class VMBaseActivity<VM : BaseViewModel>(@LayoutRes layoutId: Int = 0) :
    BaseActivity(layoutId) {

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObserve()
    }

    protected open fun viewModelObserve() {
        viewModel.loadingStatus.observe(this) {
            when (it) {
                AppConst.loading -> {
                    showLoading()
                }

                else -> {
                    dismissLoading()
                }
            }
        }

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    onLoginSucceed()
                } else {
                    onCancelLogin()
                }
            }
        viewModel.loginStatus.observe(activity) {
            launcher.launch(Intent(activity, LoginActivity::class.java))
        }
    }

    protected open fun onLoginSucceed() {
        toast("登录成功! 您继续表演!")
    }

    protected open fun onCancelLogin() {

    }

}