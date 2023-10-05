package com.example.wan.android.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.wan.android.base.dialog.LoadingDialog

abstract class BaseFragment(@LayoutRes layoutID: Int = 0) : Fragment(layoutID) {

    protected val loadingDialog by lazy { LoadingDialog(requireContext()) }

    private var isLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeBus()
    }

    override fun onResume() {
        super.onResume()
        // ViewPager2 默认可见时才执行 onResume,
        // 其它情况请使用 FragmentTransaction.setMaxLifecycle 控制 Fragment 的生命周期
        if (isLoaded.not()) {
            lazyLoad()
            isLoaded = true
        }
    }

    protected open fun lazyLoad() {

    }

    protected open fun observeBus() {

    }

    protected open fun showLoading() {
        loadingDialog.show()
    }

    protected open fun dismissLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    val fragment get() = this

}