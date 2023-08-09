package com.example.flamingo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebData(
    val id: Int,
    val url: String,
    val title: String? = null,
    var like: Boolean,
    val position: Int,
    var position2: Int? = null, // 适配二维列表
) : Parcelable
