package com.example.flamingo.base.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.flamingo.base.dialog.LoadingDialog
import com.example.flamingo.utils.hideSoftInput

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
        observeBus()
    }

    override fun finish() {
        currentFocus?.hideSoftInput()
        super.finish()
    }

    protected fun observeBus() {

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