package com.example.wan.android.utils

enum class AppPkg(val pkg: String, val app: String) {

    WanAndroid("com.example.wan.android", "WanAndroid"), // 本 APP 测试打开自定义 Scheme
    WeChat("com.tencent.mm", "微信"), // 适配打开 weixin 协议

    // 以下可适配打开 http(s) 协议
    Chrome("com.android.chrome", "Chrome"),
    Edge("com.microsoft.emmx", "Edge"),
    Firefox("org.mozilla.firefox", "Firefox"),
    XBrowser("com.mmbox.xbrowser", "X浏览器"),
    Via("mark.via", "Via"),
    Quark("com.quark.browser", "夸克"),
    Oupeng("com.oupeng.mini.android", "欧朋浏览器"), // Opera mini 的中国定制版
    UCMobile("com.UCMobile", "UC浏览器"),
    UCMobileLite("com.ucmobile.lite", "UC浏览器极速版"),
    UCMobileIntl("com.ucmobile.intl", "UC浏览器国际版"),
    QQBrowser("com.tencent.mtt", "QQ浏览器"),
    Baidu("com.baidu.searchbox", "百度"),
    BaiduLite("com.baidu.searchbox.lite", "百度极速版"),
    BaiduBrowser("com.baidu.browser.apps", "百度浏览器"),
    QihooBrowser360("com.qihoo.browser", "360浏览器"),
    QihooContents360("com.qihoo.contents", "360极速浏览器"),

}