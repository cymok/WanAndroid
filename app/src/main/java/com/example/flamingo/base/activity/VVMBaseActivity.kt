package com.example.flamingo.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.constant.AppConst

abstract class VVMBaseActivity<VM : BaseViewModel, VB : ViewBinding> : VBaseActivity<VB>() {

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObserve()
    }

    private fun viewModelObserve() {
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

}