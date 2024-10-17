package com.example.wan.android.base.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding
import com.example.wan.android.base.BaseViewModel
import com.example.wan.android.constant.AppConst
import com.example.wan.android.index.login.LoginActivity
import com.example.wan.android.utils.toast

abstract class VVMBaseActivity<VM : BaseViewModel, VB : ViewBinding> : VBaseActivity<VB>() {

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObserve()
    }

    protected var observeLoadingStatus = false

    protected open fun viewModelObserve() {
        if(observeLoadingStatus){
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