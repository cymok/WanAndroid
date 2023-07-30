package com.example.flamingo.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.flamingo.R

object ImgUtil {

    fun ImageView.load(res: Any?) {
        load(this.context, res)
    }

    fun ImageView.loadAvatar(res: Any?) {
        loadAvatar(this.context, res)
    }

    fun ImageView.load(context: Context, res: Any?) {
        Glide.with(this)
            .load(res)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(this)
    }

    fun ImageView.loadAvatar(context: Context, res: Any?) {
        Glide.with(this)
            .load(res)
            .centerCrop()
            .circleCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(this)
    }

}