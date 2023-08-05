package com.example.flamingo.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.constant.AppConst

abstract class VMBaseActivity<VM : BaseViewModel>(@LayoutRes layoutId: Int = 0) :
    BaseActivity(layoutId) {

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