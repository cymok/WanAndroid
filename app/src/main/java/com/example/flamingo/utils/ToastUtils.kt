package com.example.flamingo.utils

import android.util.Log
import com.blankj.utilcode.util.ToastUtils

fun toast(any: Any?) {
    toastShort(any.toString())
}

fun toastShort(any: Any?) {
    ToastUtils.showShort(any.toString())
    Log.e("toast", any.toString())
//    logStack(any.toString())
}

fun toastLong(any: Any?) {
    ToastUtils.showLong(any.toString())
    Log.e("toast", any.toString())
//    logStack(any.toString())
}

fun logStack(text: String?) {
    Log.e("Debug", Log.getStackTraceString(Throwable(text)))
}
