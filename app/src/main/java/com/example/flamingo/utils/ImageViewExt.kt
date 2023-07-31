package com.example.flamingo.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.flamingo.R
import com.example.flamingo.config.GlideApp

fun ImageView.loadImg(res: Any?) {
    Glide.with(this)
        .load(res)
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .into(this)
}

fun ImageView.load(res: Any?) {
    GlideApp.with(this)
        .load(res)
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .into(this)
}

fun ImageView.loadAvatar(res: Any?) {
    GlideApp.with(this)
        .load(res)
        .centerCrop()
        .circleCrop()
        .placeholder(R.mipmap.ic_launcher)
        .into(this)
}
