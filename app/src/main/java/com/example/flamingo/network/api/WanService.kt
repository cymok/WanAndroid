package com.example.flamingo.network.api

import com.example.flamingo.data.Articles
import com.example.flamingo.data.ArticlesTree
import com.example.flamingo.data.Banner
import com.example.flamingo.data.DataX
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WanService {

    /*
        // 会在 请求体 以 json 提交
        @POST("/login")
        suspend fun postTest1(@Body user: User): ApiResult<Any>

        // 请求头指定内容为 json 类型, 可直接传 String 类型的 json
        @Headers("Content-Type: application/json")
        @POST("/login")
        suspend fun postTest2(@Body json: String): ApiResult<Any>

        // 以表单方式提交
        @FormUrlEncoded
        @POST("user/edit")
        @POST("/login")
        suspend fun postTest3(
            @Field("username") username: String,
            @Field("password") password : String,
        ): ApiResult<Any>
    */

    /*
        @POST("/xxx/xxx")
        suspend fun uploadTest(@Part part: List<MultipartBody.Part>): ApiResult<String>

        // [Part] 后面支持三种类型，[RequestBody]、[MultipartBody.Part] 、任意类型
        // 除 [MultipartBody.Part] 以外，其它类型都必须带上表单字段
        // ([MultipartBody.Part] 中已经包含了表单字段的信息)
        @POST("/upload/path")
        @Multipart
        suspend fun uploadTest(
            @Part("name") name: RequestBody,
            @Part filePart: MultipartBody.Part,
        ): ApiResult<String>
    */

    // banner
    @GET("/banner/json")
    suspend fun getBanner(): ApiResult<Banner>

    // 置顶文章
    @GET("/article/top/json")
    suspend fun getHomeTopList(): ApiResult<List<DataX>>

    // 首页文章列表
    @GET("/article/list/{page}/json")
    suspend fun getHomeList(
        @Path("page") page: Int = 0,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    // 项目分类
    @GET("/project/tree/json")
    suspend fun getProjectTree(): ApiResult<ArticlesTree>

    // 项目列表数据
    @GET("/project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int = 1,
        @Query("cid") id: Int,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    // 最新项目 Tab (可以作为一个分类 插在'项目分类'最前面)
    @GET("/article/listproject/0/json")
    suspend fun getNewProjectList(
        @Path("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    @GET("/wxarticle/chapters/json")
    suspend fun getWxArticleTree(): ApiResult<ArticlesTree>

    // 获取公众号列表
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getWxArticleList(
        @Path("id") id: Int,
        @Path("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    // 查看某个公众号历史数据
    @GET("/wxarticle/list/{id}/{page}}/json")
    suspend fun getWhoWxArticleList(
        @Path("id") id: Int,
        @Path("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    // 在某个公众号中搜索历史文章
    @GET("/wxarticle/list/405/1/json?k=Java")
    suspend fun searchWxArticleList(
        @Path("id") id: Int,
        @Path("page") page: Int = 1,
        @Query("k") k: Int,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    // 搜索 (支持多个关键词，用空格隔开)
    @POST("/article/query/{page}/json")
    suspend fun search(
        @Path("page") page: Int = 0,
        @Query("page_size") pageSize: Int = 10,
        @Path("k") k: String,
    ): ApiResult<Articles>

    // 问答
    @GET("/wenda/list/{page}/json")
    suspend fun getQAList(
        @Path("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

    // 问答评论列表
    @GET("/wenda/comments/问答id/json")
    suspend fun getQACommentList(
        @Path("id") id: Int = 1,
        @Query("page_size") pageSize: Int = 10,
    ): ApiResult<Articles>

}