package com.example.wan.android.utils

import android.annotation.SuppressLint
import android.util.Log
import com.blankj.utilcode.util.LogUtils

@Suppress("NOTHING_TO_INLINE")
inline fun log(any: Any?, tag: String? = "log") {
    loge(any = any, tag = tag)
}

@SuppressLint("LogUsage")
@Suppress("NOTHING_TO_INLINE")
inline fun logStack(text: String?) {
//    Log.e("logStack", Log.getStackTraceString(Throwable(text)))
    Log.e("logStack", text, Throwable(text))
}

@SuppressLint("LogUsage")
@Suppress("NOTHING_TO_INLINE")
inline fun logv(any: Any?, tag: String? = "log") {
    Log.v(tag, any.toString())
}

@SuppressLint("LogUsage")
@Suppress("NOTHING_TO_INLINE")
inline fun logd(any: Any?, tag: String? = "log") {
    Log.d(tag, any.toString())
}

@SuppressLint("LogUsage")
@Suppress("NOTHING_TO_INLINE")
inline fun logi(any: Any?, tag: String? = "log") {
    Log.i(tag, any.toString())
}

@SuppressLint("LogUsage")
@Suppress("NOTHING_TO_INLINE")
inline fun logw(any: Any?, tag: String? = "log") {
    Log.w(tag, any.toString())
}

@SuppressLint("LogUsage")
@Suppress("NOTHING_TO_INLINE")
inline fun loge(any: Any?, tag: String? = "log") {
    Log.e(tag, any.toString())
    if (any is Throwable) {
        LogUtils.eTag(tag, any)
//        any.printStackTrace()
    }
}
