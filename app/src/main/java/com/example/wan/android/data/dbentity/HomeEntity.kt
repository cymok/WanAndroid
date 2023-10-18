package com.example.wan.android.data.dbentity

import org.litepal.crud.LitePalSupport

data class CacheEntity(
    val json: String,
    val apiName: String,
    val page: String = "",
    val arg1: String = "",
    val arg2: String = "",
    val arg3: String = "",
) : LitePalSupport()
