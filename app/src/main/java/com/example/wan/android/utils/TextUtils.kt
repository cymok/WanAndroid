package com.example.wan.android.utils

object TextUtils {

    // 参考 `android.text.TextUtils.htmlEncode`
    fun String.htmlDecode(): String {
        return this.replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&#39;", "'")
            //http://www.w3.org/TR/xhtml1
            // The named character reference &apos; (the apostrophe, U+0027) was introduced in
            // XML 1.0 but does not appear in HTML. Authors should therefore use &#39; instead
            // of &apos; to work as expected in HTML 4 user agents.
            // 兼容 &apos;
            .replace("&apos;", "'")
            .replace("&quot;", "\"")
    }

}