package com.example.flamingo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebData(
    val id: Int,
    val url: String,
    val title: String?,
) : Parcelable
