package com.example.flamingo.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.flamingo.base.dialog.LoadingDialog

abstract class BaseFragment(@LayoutRes layoutID: Int = 0) : Fragment(layoutID) {

    protected val loadingDialog by lazy { LoadingDialog(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeBus()
    }

    private fun observeBus() {

    }

    fun showLoading() {
        loadingDialog.show()
    }

    fun dismissLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

}