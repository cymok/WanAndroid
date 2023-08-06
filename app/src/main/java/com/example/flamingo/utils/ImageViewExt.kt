package com.example.flamingo.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.example.flamingo.R
import com.example.flamingo.config.GlideApp

fun ImageView.load(any: Any?) {
    GlideApp.with(this)
        .load(any)
        .centerCrop()
//        .placeholder(R.mipmap.ic_launcher)
        .into(this)
}

/**
 * 资源是 Int 类型, 用 `load(any:Any)` 经常会加载错别的图
 */
fun ImageView.loadRes(@RawRes @DrawableRes res: Int) {
    GlideApp.with(this)
        .load(res)
        .centerCrop()
//        .placeholder(R.mipmap.ic_launcher)
        .into(this)
}

fun ImageView.loadAvatar(res: Any?) {
    GlideApp.with(this)
        .load(res)
        .centerCrop()
        .circleCrop()
//        .placeholder(R.mipmap.ic_launcher)
        .into(this)

}
