package com.example.flamingo.utils

import android.util.Log
import com.blankj.utilcode.util.ToastUtils

fun toast(text: CharSequence?) {
    toastShort(text)
}

fun toastShort(text: CharSequence?) {
    ToastUtils.showShort(text)
    Log.e("toast", text.toString())
}

fun toastLong(text: CharSequence?) {
    ToastUtils.showLong(text)
    Log.e("toast", text.toString())
}

fun logStack(text: CharSequence?) {
    Log.e("Debug", Log.getStackTraceString(Throwable("$text")))
}
