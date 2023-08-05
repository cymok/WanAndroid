package com.example.flamingo.network

import com.example.flamingo.network.api.WanAndroidService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private const val BASE_URL = "https://www.wanandroid.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(LoggingInterceptor())

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = builder.build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    val wanApiService: WanAndroidService = retrofit.create(WanAndroidService::class.java)

}
