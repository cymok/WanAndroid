package com.example.wan.android.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.example.wan.android.App

fun Activity.startBrowser(url: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }

    when (Uri.parse(url).scheme) {
        // setPackage 设置一个包名 指定打开某个浏览器 只能设置一个
        "weixin" -> {
            // 适配 weixin 的 scheme，跳转到微信 APP 打开
            intent.setPackage(AppPkg.WeChat.pkg)
        }

        "wanandroid" -> {
            intent.setPackage(AppPkg.WanAndroid.pkg)
        }
    }

    // FIXME
    // MIUI 12 测试 `Intent.createChooser` 选择浏览器无效，只会打开 `intent.setPackage` 指定的浏览器或系统设置的默认浏览器
    if (intent.resolveActivity(App.INSTANCE.packageManager) != null) {
        startActivity(Intent.createChooser(intent, "选择浏览器"))
    } else {
        toast("没有可用的浏览器应用")
    }

    // MIUI 12 测试 无论如何都只能使用系统设置的默认浏览器打开
//    val resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
//    if (resolveInfoList.isNotEmpty()) {
//        val defaultBrowserPackageName = resolveInfoList[0].activityInfo.packageName
//        intent.setPackage(defaultBrowserPackageName)
//        startActivity(intent)
//    } else {
//        toast("没有可用的浏览器应用")
//    }

}
