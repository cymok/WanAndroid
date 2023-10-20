package com.example.wan.android.data

import android.os.Parcelable
import com.example.wan.android.utils.TextUtils.htmlDecode
import kotlinx.parcelize.Parcelize

class ArticlesTree : ArrayList<ArticlesTreeItem>()

@Parcelize
data class ArticlesTreeItem(
    val articleList: List<Articles>, // 返回的 list 是空的 类型是我瞎猜的
    val author: String,
    val children: List<ArticlesTreeItem>, // 返回的 list 是空的 类型是我瞎猜的
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    @Deprecated("使用 nameDecoded 获取")
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Parcelable {
    val nameDecoded: String get() = name.htmlDecode()
}
