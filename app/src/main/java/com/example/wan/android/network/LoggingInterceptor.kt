package com.example.wan.android.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        val msgRequest = "request(${request.method}): ${request.url}\n" +
//                "${request.headers.toStringCustom().trim()}\n" +
                getRequestInfo(request)
        Log.e(TAG, msgRequest)

        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        val msgResponse = "response for ${response.request.url} in ${(t2 - t1) / 1e6} ms\n" +
//                "${response.headers.toStringCustom().trim()}\n" +
//                getResponseInfo(response)
                // 太长的内容 在新版 Android Studio 的 Logcat 显示得很恶心
                getResponseInfo(response).let {
                    val maxLen = 180
                    if (it.length > maxLen) {
                        it.substring(0, maxLen)
                    } else {
                        it
                    }
                }
        Log.e(TAG, msgResponse)

        // 判断数据来源
        val isFromCache = response.cacheResponse != null
        val isFromNetwork = response.networkResponse != null
        if (isFromCache) {
            Log.i("LoggingInterceptor", "response from cache")
        } else if (isFromNetwork) {
            Log.i("LoggingInterceptor", "response from network")
        }

        return response
    }

    /**
     * 打印返回消息
     * @param response 返回的对象
     */
    private fun getResponseInfo(response: Response?): String {
        var str = ""
        if (response == null) {
            return ""
        }
        if (!response.isSuccessful) {
            return response.body?.string() ?: ""
        }
        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
        val source = responseBody.source()
        try {
            source.request(Long.MAX_VALUE) // Buffer the entire body.
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val buffer: Buffer = source.buffer
        val charset: Charset = Charset.forName("utf-8")
        if (contentLength != 0L) {
            str = buffer.clone().readString(charset)
        }
        return str
    }

    /**
     * 打印请求体
     * @param request 请求的对象
     */
    private fun getRequestInfo(request: Request): String {
        val requestBody = request.body
        val buffer = Buffer()
        try {
            if (requestBody != null) {
                requestBody.writeTo(buffer)
            } else {
                return ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }

        var charset = Charset.forName("UTF-8")
        val contentType = requestBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(Charset.forName("UTF-8"))
        }

        return buffer.readString(charset)
    }

    companion object {
        const val TAG = "LoggingInterceptor"
    }
}
