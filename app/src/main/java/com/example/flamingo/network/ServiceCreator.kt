package com.example.flamingo.network

import com.example.flamingo.App
import com.example.flamingo.network.api.LikeService
import com.example.flamingo.network.api.SquareService
import com.example.flamingo.network.api.UserService
import com.example.flamingo.network.api.WanService
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private const val BASE_URL = "https://www.wanandroid.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.INSTANCE)))
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
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

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    val wanApiService = retrofit.create(WanService::class.java)
    val userApiService = retrofit.create(UserService::class.java)
    val squareApiService = retrofit.create(SquareService::class.java)
    val likeApiService = retrofit.create(LikeService::class.java)

}
