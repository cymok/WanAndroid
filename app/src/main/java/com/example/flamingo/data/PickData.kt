package com.example.flamingo.data

import android.net.Uri

data class PickData(
    val crop: Boolean,
    val uri: Uri,
    val path: String,
)
