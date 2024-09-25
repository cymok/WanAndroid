package com.example.wan.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.blankj.utilcode.util.ConvertUtils

inline val Float.dp2px get() = ConvertUtils.dp2px(this)
inline val Float.px2dp get() = ConvertUtils.px2dp(this)
inline val Float.sp2px get() = ConvertUtils.sp2px(this)
inline val Float.px2sp get() = ConvertUtils.px2sp(this)

inline val Int.dp2px get() = ConvertUtils.dp2px(this.toFloat())
inline val Int.px2dp get() = ConvertUtils.px2dp(this.toFloat())
inline val Int.sp2px get() = ConvertUtils.sp2px(this.toFloat())
inline val Int.px2sp get() = ConvertUtils.px2sp(this.toFloat())

// compose px2dp
@Composable
fun Float.px2dp() = with(LocalDensity.current) { this@px2dp.toDp() }

// compose px2dp
@Composable
fun Int.px2dp() = with(LocalDensity.current) { this@px2dp.toDp() }

// compose px2sp
@Composable
fun Float.px2sp() = with(LocalDensity.current) { this@px2sp.toSp() }

// compose px2sp
@Composable
fun Int.px2sp() = with(LocalDensity.current) { this@px2sp.toSp() }

// compose dp2px
@Composable
fun Dp.dp2px() = with(LocalDensity.current) { this@dp2px.toPx() }

// compose sp2px
@Composable
fun TextUnit.sp2px() = with(LocalDensity.current) { this@sp2px.toPx() }

