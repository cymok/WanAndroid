package com.example.wan.android.constant

import com.blankj.utilcode.util.PathUtils
import java.io.File

object AppConst {

    val crashPath by lazy { PathUtils.getFilesPathExternalFirst() + File.separator + "crash" }
    val okhttpCachePath by lazy { PathUtils.getCachePathExternalFirst() + File.separator + "okhttp" }
    const val OKHTTP_CACHE_SIZE: Long = 1024 * 1024 * 64 // 64 MB
    val glidePath by lazy { PathUtils.getCachePathExternalFirst() + File.separator + "glide" }
    val coilPath by lazy { PathUtils.getCachePathExternalFirst() + File.separator + "coil" }
    const val IMAGE_CACHE_SIZE: Long = 1024 * 1024 * 256 // 256 MB

    const val refresh = 1
    const val loading = 2
    const val complete = 3
    const val error = 4
    const val loadMore = 5
    const val loadComplete = 6
    const val loadMoreFail = 7
    const val noMore = 8

}