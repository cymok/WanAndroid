package com.example.wan.android.data.model

import android.os.Parcelable
import com.example.wan.android.utils.TextUtils.htmlDecode
import kotlinx.parcelize.Parcelize

@Parcelize
data class Articles(
    val curPage: Int,
    val datas: MutableList<DataX>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
) : Parcelable

@Parcelize
data class DataX(
    val adminAdd: Boolean,
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    @Deprecated("使用 chapterNameDecoded 获取")
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val host: String,
    val id: Int,
    val isAdminAdd: Boolean,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val originId: Int,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: ArrayList<Tag>?,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
) : Parcelable {
    val chapterNameDecoded get() = chapterName.htmlDecode()
}

@Parcelize
data class Tag(
    val name: String,
    val url: String
) : Parcelable