package com.example.flamingo.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.constant.AppConst

abstract class VMBaseFragment<VM : BaseViewModel>(@LayoutRes layoutId: Int = 0) : BaseFragment(layoutId) {

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserve()
    }

    private fun viewModelObserve() {
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

}