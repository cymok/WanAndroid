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

    // 设置一个包名 指定打开某个浏览器
    intent.setPackage("com.android.chrome") // Chrome
//    intent.setPackage("com.microsoft.emmx") // Edge
//    intent.setPackage("org.mozilla.firefox") // Firefox
//    intent.setPackage("com.mmbox.xbrowser") // X浏览器
//    intent.setPackage("mark.via") // Via
//    intent.setPackage("com.quark.browser") // 夸克
//    intent.setPackage("com.oupeng.mini.android") // 欧朋浏览器 (Opera mini 的中国定制版)
//    intent.setPackage("com.UCMobile") // UC浏览器
//    intent.setPackage("com.ucmobile.lite") // UC浏览器极速版
//    intent.setPackage("com.UCMobile.intl") // UC浏览器 国际版
//    intent.setPackage("com.tencent.mtt") // QQ浏览器
//    intent.setPackage("com.baidu.searchbox") // 百度
//    intent.setPackage("com.baidu.searchbox.lite") // 百度极速版
//    intent.setPackage("com.baidu.browser.apps") // 百度浏览器
//    intent.setPackage("com.qihoo.browser") // 360浏览器
//    intent.setPackage("com.qihoo.contents") // 360极速浏览器

    // FIXME
    // MIUI 12 测试 `Intent.createChooser` 选择浏览器无效，只会打开 `intent.setPackage` 指定的浏览器或系统设置的默认浏览器
    if (intent.resolveActivity(App.INSTANCE.packageManager) != null) {
        startActivity(Intent.createChooser(intent, "选择浏览器"))
    } else {
        toast("没有可用的浏览器应用")
    }
}
