package com.example.wan.android.utils

import com.blankj.utilcode.util.ConvertUtils
import java.io.Serializable

fun Serializable.toByteArray(): ByteArray {
    return ConvertUtils.serializable2Bytes(this)
}

fun ByteArray.toObject(): Any {
    return ConvertUtils.bytes2Object(this)
}
