package com.example.wan.android.network

import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CacheInterceptor : Interceptor {

    companion object {
        /*
        当max-age 与 max-stale 和 min-fresh 同时使用时, 它们的设置相互之间独立生效, 最为保守的缓存策略总是有效。
        即哪个过期时间最早，就在这个过期时间后，发起资源请求，重新向服务端做验证。
        */

        // 缓存资源, 但是在指定时间(单位为秒)后缓存过期
        private const val maxAge = 60 * 60 * 24 * 0.5

        // 指定时间内, 即使缓存过时, 资源依然有效
        private const val maxStale = 60 * 60 * 24 * 7
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        return if (NetworkUtils.isAvailable()) {

            val response = chain.proceed(request)
            val cacheControl = request.cacheControl.toString()

            Log.i("CacheInterceptor", maxAge.toString() + "s load cache" + cacheControl)

            response.newBuilder()
                .removeHeader("Pragma")
                .header(
                    "Cache-Control",
                    "public, max-age=$maxAge"
                )
                .build()
        } else {

            Log.i("CacheInterceptor", "no network load cache")

            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()

            val response = chain.proceed(request)
            response.newBuilder()
                .removeHeader("Pragma")
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$maxStale"
                )
                .build()
        }
    }

}