package com.example.wan.android.utils

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.GsonBuilder

/**
 * 默认 不进行 Http 转义
 * 默认 格式化 Json 字符串
 */
fun Any.toJson(escape: Boolean = false, format: Boolean = true): String? {
    return try {
        val gson = GsonBuilder().apply {
            if (escape.not()) {
                disableHtmlEscaping()
            }
            if (format) {
                setPrettyPrinting()
            }
        }.create()
        GsonUtils.toJson(gson, this)
    } catch (e: Exception) {
        loge(e)
        null
    }
}

inline fun <reified T> fromJson(json: String?): T? {
    return try {
        GsonUtils.fromJson(GsonUtils.getGson(), json, T::class.java)
    } catch (e: Exception) {
        loge(e)
        null
    }
}

inline fun <reified T> json2Instance(json: String?) = fromJson<T>(json)

inline fun <reified T> json2Object(json: String?) = fromJson<T>(json)
