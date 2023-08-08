package com.example.flamingo.network

import android.util.Log
import com.blankj.utilcode.util.SPUtils
import okhttp3.Interceptor
import okhttp3.Response

@Deprecated("新版加密了 已经不能直接读取到值了 用 [CookieJar]")
class CookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // --- 从 request 读取 cookies
        val request = chain.request()
        val requestWithCookies = request.newBuilder().apply {
            val cookies = SPUtils.getInstance("Set-Cookie").getStringSet("cookies")
            Log.e("cookies", "request cookies.size = ${cookies.size} \njson = ${request.headers}")
            cookies.forEach {
                header("Cookie", it)
            }
        }.build()

        // --- 从 response 保存 cookies
        val response = chain.proceed(requestWithCookies)
        if (response.headers("Set-Cookie").isNotEmpty()) {
            val cookies = mutableSetOf<String>()
            response.headers("Set-Cookie").forEach {
                cookies.add(it)
            }
            Log.e("cookies", "response cookies.size = ${cookies.size} \njson = ${response.headers}")
            SPUtils.getInstance("Set-Cookie").put("cookies", cookies)
        }

        return response
    }
}