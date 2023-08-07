package com.example.flamingo.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.example.flamingo.config.GlideApp

fun ImageView.load(any: Any?, @DrawableRes placeholderRes: Int = 0) {
    GlideApp.with(this)
        .load(any)
        .centerCrop()
        .placeholder(placeholderRes)
        .into(this)
}

/**
 * 资源是 Int 类型, 用 `load(any:Any)` 经常会加载错别的图
 */
fun ImageView.loadRes(@RawRes @DrawableRes res: Int, @DrawableRes placeholderRes: Int = 0) {
    GlideApp.with(this)
        .load(res)
        .centerCrop()
        .placeholder(placeholderRes)
        .into(this)
}

fun ImageView.loadCircle(res: Any?, @DrawableRes placeholderRes: Int = 0) {
    GlideApp.with(this)
        .load(res)
        .centerCrop()
        .circleCrop()
        .placeholder(placeholderRes)
        .into(this)

}
