package com.example.wan.android.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.example.wan.android.config.GlideApp
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

fun ImageView.load(
    any: Any?,
    cornerRadius: Int = 0,
    @DrawableRes placeholderRes: Int = 0
) {
    GlideApp.with(this)
        .load(any)
        .centerCrop()
        .transform(RoundedCornersTransformation(cornerRadius, 0))
        .placeholder(placeholderRes)
        .into(this)
}

fun ImageView.loadRes(
    @RawRes @DrawableRes res: Int,
    cornerRadius: Int = 0,
    @DrawableRes placeholderRes: Int = 0
) {
    GlideApp.with(this)
        .load(res)
        .centerCrop()
        .transform(RoundedCornersTransformation(cornerRadius, 0))
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
