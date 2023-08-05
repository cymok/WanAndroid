package com.example.flamingo.network.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface WanAndroidService {

    @POST("/xxx/xxx")
    suspend fun uploadTest(@Part part: List<MultipartBody.Part>): ApiResult<String>

    /**
     * [Part] 后面支持三种类型，[RequestBody]、[MultipartBody.Part] 、任意类型
     * 除 [MultipartBody.Part] 以外，其它类型都必须带上表单字段
     * ([MultipartBody.Part] 中已经包含了表单字段的信息)
     */
    @POST("/upload/path")
    @Multipart
    suspend fun uploadTest(
        @Part("name") name: RequestBody,
        @Part filePart: MultipartBody.Part
    ): ApiResult<String>

    @POST("/user/logout/json")
    suspend fun register(@FieldMap params: Map<String, Any>): ApiResult<String>

    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResult<String>

    @POST("/user/logout/json")
    suspend fun logout(): ApiResult<String>

    @GET("/user_article/list/{page}/json")
    suspend fun getSquareList(@Path("page") page: Int = 0, page_size: Int = 10): ApiResult<String>

}