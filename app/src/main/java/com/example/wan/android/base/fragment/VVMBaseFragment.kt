package com.example.wan.android.base.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.example.wan.android.base.BaseViewModel
import com.example.wan.android.constant.AppConst
import com.example.wan.android.index.login.LoginActivity
import com.example.wan.android.utils.toast

abstract class VVMBaseFragment<VM : BaseViewModel, VB : ViewBinding> : VBaseFragment<VB>() {

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserve()
    }

    protected open var observeLoadingStatus = false

    protected open fun viewModelObserve() {
        if(observeLoadingStatus){
            viewModel.loadingStatus.observe(viewLifecycleOwner) {
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
        viewModel.loginStatus.observe(viewLifecycleOwner) {
            launcher.launch(Intent(activity, LoginActivity::class.java))
        }
    }

    protected open fun onLoginSucceed() {
        toast("登录成功! 您继续表演!")
    }

    protected open fun onCancelLogin() {

    }

}