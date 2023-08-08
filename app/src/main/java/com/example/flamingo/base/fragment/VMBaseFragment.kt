package com.example.flamingo.base.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.flamingo.base.BaseViewModel
import com.example.flamingo.constant.AppConst
import com.example.flamingo.index.login.LoginActivity
import com.example.flamingo.utils.toast

abstract class VMBaseFragment<VM : BaseViewModel>(@LayoutRes layoutId: Int = 0) :
    BaseFragment(layoutId) {

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserve()
    }

    protected open fun viewModelObserve() {
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

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    toast("登录成功! 您继续表演!")
                }
            }
        viewModel.loginStatus.observe(viewLifecycleOwner) {
            launcher.launch(Intent(activity, LoginActivity::class.java))
        }
    }

}