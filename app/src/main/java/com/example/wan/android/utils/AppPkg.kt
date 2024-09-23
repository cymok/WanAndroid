package com.example.wan.android.utils

enum class AppPkg(val pkg: String, val app: String) {

//    intent.setPackage("com.tencent.mm") // 微信
//    intent.setPackage("com.android.chrome") // Chrome
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

    WANANDROID("com.example.wan.android", "WanAndroid"), // 本 APP 测试打开自定义 Scheme
    WECHAT("com.tencent.mm", "微信"), // 适配打开 weixin 协议

    // 以下可适配打开 http(s) 协议
    Chrome("com.android.chrome", "Chrome"),
    Edge("com.microsoft.emmx", "Edge"),
    Firefox("org.mozilla.firefox", "Firefox"),
    XBrowser("com.mmbox.xbrowser", "X浏览器"),
    Via("mark.via", "Via"),
    Quark("com.quark.browser", "夸克"),
    Oupeng("com.oupeng.mini.android", "欧朋浏览器"),
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