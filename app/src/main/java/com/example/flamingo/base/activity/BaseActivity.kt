package com.example.flamingo.base.activity

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.blankj.utilcode.util.SPUtils
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

    protected open fun initStatusBarDarkFont(): Boolean {
        // 非深色模式都默认设置黑色字体
        val lightModel = SPUtils.getInstance().getInt("night_module")
        return lightModel != AppCompatDelegate.MODE_NIGHT_YES
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        immersionBar {
            // 低版本系统状态栏字体不会自动变色
            statusBarDarkFont(initStatusBarDarkFont())
        }
    }

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