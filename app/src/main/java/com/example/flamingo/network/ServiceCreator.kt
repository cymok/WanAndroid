package com.example.flamingo.network

import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.network.api.WanAndroidService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private const val BASE_URL = "https://www.wanandroid.com/"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(LoggingInterceptor())

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    val wanApiService: WanAndroidService = retrofit.create(WanAndroidService::class.java)

    class LoggingInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val t1 = System.nanoTime()
            LogUtils.eTag(
                TAG,
                "request(${request.method}): ${request.url}\n" +
                        "${request.headers.toString().trim()}\n" +
                        getRequestInfo(request)
            )

            val response = chain.proceed(request)
            val t2 = System.nanoTime()
            LogUtils.eTag(
                TAG,
                "response for ${response.request.url} in ${(t2 - t1) / 1e6} ms\n" +
                        getResponseInfo(response)
            )

            return response
        }

        /**
         * 打印返回消息
         * @param response 返回的对象
         */
        private fun getResponseInfo(response: Response?): String {
            var str = ""
            if (response == null || !response.isSuccessful) {
                return str
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

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder().apply {
/*
                // 全局 Header
                header("Authorization", UserUtils.getToken())
                val map = mapOf(
                    "deviceModel" to "${DeviceUtils.getManufacturer()}_${DeviceUtils.getModel()}", // 手机型号
                    "systemVersion" to "Android_${DeviceUtils.getSDKVersionName()}", // 系统版本
                    "uniqueSign" to DeviceUtils.getUniqueDeviceId(), // 设备唯一标记
                )
                header("User-Agent", GsonUtils.toJson(map))
*/
            }.build()
            return chain.proceed(request)
        }
    }

}