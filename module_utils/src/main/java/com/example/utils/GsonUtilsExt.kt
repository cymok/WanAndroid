package com.example.utils

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.GsonBuilder

/**
 * 默认 不进行 Http 转义
 * 默认 格式化 Json 字符串
 */
fun Any.toJson(escape: Boolean = false, format: Boolean = true): String {
    val gson = GsonBuilder().apply {
        if (escape.not()) {
            disableHtmlEscaping()
        }
        if (format) {
            setPrettyPrinting()
        }
    }.create()
    return GsonUtils.toJson(gson, this)
}
