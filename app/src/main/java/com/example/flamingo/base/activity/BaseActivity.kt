package com.example.flamingo.base.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.flamingo.R
import com.example.flamingo.base.dialog.LoadingDialog
import com.example.flamingo.utils.hideSoftInput
import com.gyf.immersionbar.ktx.immersionBar

abstract class BaseActivity(@LayoutRes layoutId: Int = 0) : AppCompatActivity(layoutId) {

    protected val loadingDialog by lazy { LoadingDialog(this) }

    val isInMultiWindow: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                isInMultiWindowMode
            } else {
                false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 状态栏导航栏颜色
        immersionBar {
            statusBarColor(initStatusBarColor())
            navigationBarColor(initNavBarColor())
            statusBarDarkFont(initStatusBarDarkFont())
        }

        observeBus()
    }

    @ColorRes
    protected open fun initStatusBarColor(): Int = R.color.transparent

    @ColorRes
    protected open fun initNavBarColor(): Int = R.color.black

    protected open fun initStatusBarDarkFont() = false

    override fun finish() {
        currentFocus?.hideSoftInput()
        super.finish()
    }

    protected open fun observeBus() {

    }

    fun showLoading() {
        loadingDialog.show()
    }

    fun dismissLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    val activity get() = this

}