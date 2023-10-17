package com.example.wan.android.utils

import com.blankj.utilcode.util.ToastUtils

@Suppress("NOTHING_TO_INLINE")
inline fun toast(any: Any?) {
    toastShort(any)
}

@Suppress("NOTHING_TO_INLINE")
inline fun toastShort(any: Any?) {
    ToastUtils.showShort(any.toString())
    log(any)
}

@Suppress("NOTHING_TO_INLINE")
inline fun toastLong(any: Any?) {
    ToastUtils.showLong(any.toString())
    log(any)
}
