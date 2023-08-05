package com.example.flamingo.base.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.constant.AppConst

abstract class VVMBaseFragment<VM : BaseViewModel, VB : ViewBinding> : VBaseFragment<VB>() {

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserve()
    }

    private fun viewModelObserve() {
        viewModel.loadingStatus.observe(this as LifecycleOwner) {
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