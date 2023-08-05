package com.example.flamingo.network.api

import com.example.flamingo.data.Articles
import com.example.flamingo.data.ArticlesTree
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


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
        @Part filePart: MultipartBody.Part,
    ): ApiResult<String>

    @POST("/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String,
    ): ApiResult<String>

    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): ApiResult<String>

    @GET("/user/logout/json")
    suspend fun logout(): ApiResult<String>

    @GET("/article/list/{page}/json")
    suspend fun getHomeList(
        @Path("page") page: Int = 0,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    @GET("/project/tree/json")
    suspend fun getProjectTree(): ApiResult<ArticlesTree>

    @GET("/project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int = 1,
        @Query("cid") id: Int,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    @GET("/user_article/list/{page}/json")
    suspend fun getSquareList(
        @Path("page") page: Int = 0,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    @GET("/wxarticle/chapters/json")
    suspend fun getWxArticleTree(): ApiResult<ArticlesTree>

    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getWxArticleList(
        @Path("id") id: Int,
        @Path("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    @GET("/wenda/list/{page}/json")
    suspend fun getQAList(
        @Path("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

}