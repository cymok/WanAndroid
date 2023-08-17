package com.example.wan.android.data

class Banner : ArrayList<BannerItem>()

data class BannerItem(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String,
    var collect: Boolean, // 接口没有返回这个字段的
)