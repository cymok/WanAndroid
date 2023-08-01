package com.example.flamingo.utils

import android.widget.ImageView
import com.example.aar.ModuleLintAar
import com.example.flamingo.R
import com.example.flamingo.config.GlideApp

fun ImageView.load(res: Any?) {

    ModuleLintAar.test()

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
