package com.example.wan.android.utils

import com.blankj.utilcode.util.ConvertUtils

inline val Float.dp2px get() = ConvertUtils.dp2px(this)
inline val Float.px2dp get() = ConvertUtils.px2dp(this)
inline val Float.sp2px get() = ConvertUtils.sp2px(this)
inline val Float.px2sp get() = ConvertUtils.px2sp(this)

inline val Int.dp2px get() = ConvertUtils.dp2px(this.toFloat())
inline val Int.px2dp get() = ConvertUtils.px2dp(this.toFloat())
inline val Int.sp2px get() = ConvertUtils.sp2px(this.toFloat())
inline val Int.px2sp get() = ConvertUtils.px2sp(this.toFloat())
