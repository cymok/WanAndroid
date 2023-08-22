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
        //网络可用时，使用缓存过期时间，单位秒
        private const val maxAge = 30

        //网络不可用时
        private const val maxStale = 60 * 60 * 24 * 3
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        return if (NetworkUtils.isAvailable()) {

            val response: Response = chain.proceed(request)
            val cacheControl = request.cacheControl.toString()

            Log.i("CacheInterceptor", maxAge.toString() + "s load cache" + cacheControl)

            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
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
            val response: Response = chain.proceed(request)
            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$maxStale"
                )
                .build()
        }
    }

}