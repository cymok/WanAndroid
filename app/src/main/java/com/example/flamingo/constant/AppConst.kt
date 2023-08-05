package com.example.flamingo.constant

import com.blankj.utilcode.util.PathUtils
import java.io.File

object AppConst {
    const val WX_APP_ID = ""
    const val imageCacheSize: Long = 1024 * 1024 * 512 // 512 MB
    val glidePath by lazy { PathUtils.getCachePathExternalFirst() + File.separator + "glide" }
    val coilPath by lazy { PathUtils.getCachePathExternalFirst() + File.separator + "coil" }


    const val refresh = 1
    const val loading = 2
    const val complete = 3
    const val error = 4
    const val loadMore = 5
    const val loadComplete = 6
    const val loadMoreFail = 7
    const val noMore = 8

}