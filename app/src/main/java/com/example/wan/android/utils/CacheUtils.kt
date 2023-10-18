package com.example.wan.android.utils

import com.example.wan.android.data.dbentity.CacheEntity

fun save(json: String, vararg conditions: String) {
    CacheEntity(apiName = "apiName", json = json)
        .saveOrUpdate(*conditions)
}
