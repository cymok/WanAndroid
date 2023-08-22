package com.example.wan.android.network

import com.example.wan.android.App
import com.example.wan.android.constant.AppConst
import com.example.wan.android.network.api.LikeService
import com.example.wan.android.network.api.SquareService
import com.example.wan.android.network.api.UserService
import com.example.wan.android.network.api.WanService
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private const val BASE_URL = "https://www.wanandroid.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.INSTANCE)))
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .cache(Cache(File(AppConst.okhttpCachePath), AppConst.OKHTTP_CACHE_SIZE))
        .addNetworkInterceptor(CacheInterceptor())
        .retryOnConnectionFailure(true) // 默认重试一次，若需要重试N次，则要实现拦截器。
//        .addInterceptor(HeaderInterceptor())
//        .addInterceptor(CookiesInterceptor())
        .addInterceptor(LoggingInterceptor())

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
            .disableHtmlEscaping() // 禁止 Html 转义
            .create()))

    private val retrofit = builder.build()

    inline fun <reified T> Retrofit.create(): T = this.create(T::class.java)

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    val wanApiService = retrofit.create(WanService::class.java)
    val userApiService = retrofit.create(UserService::class.java)
    val squareApiService = retrofit.create(SquareService::class.java)
    val likeApiService = retrofit.create(LikeService::class.java)

}
