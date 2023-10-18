package com.example.wan.android.network.repository

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.NetworkUtils
import com.example.wan.android.data.Articles
import com.example.wan.android.data.BannerItem
import com.example.wan.android.data.DataX
import com.example.wan.android.data.dbentity.CacheEntity
import com.example.wan.android.network.ServiceCreator
import com.example.wan.android.utils.log
import com.example.wan.android.utils.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litepal.LitePal

/**
 * 单一数据源 给 ViewModel 提供数据
 */
object WanRepository {

    private val apiService by lazy { ServiceCreator.wanApiService }
    private val userService by lazy { ServiceCreator.userApiService }
    private val squareService by lazy { ServiceCreator.squareApiService }
    private val likeService by lazy { ServiceCreator.likeApiService }

    suspend fun register(username: String, password: String) = withContext(Dispatchers.IO) {
        userService.register(username, password, password).apiData()!!
    }

    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        userService.login(username, password).apiData()!!
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        // null
        userService.logout().apiData()
    }

    suspend fun getUserInfo() = withContext(Dispatchers.IO) {
        userService.getUserInfo().apiData()!!
    }

    suspend fun getBanner() = withContext(Dispatchers.IO) {
        val apiName = "bannerList"
        if (NetworkUtils.isAvailable()) {
            val bannerList = apiService.getBanner().apiData()!!
            CacheEntity(json = bannerList.toJson(), apiName = apiName)
                .apply { saveOrUpdate("apiName = ?", apiName) }
            bannerList
        } else {
            // Gson().fromJson() 将 json 转回 list 或 array
            // object : TypeToken<ArrayList<BannerItem>>() {} // List 要用 TypeToken
            // Array<BannerItem>::class.java // Array 可以直接使用对象 class
            val cache = LitePal.where("apiName = ?", apiName)
                .findFirst(CacheEntity::class.java)
            val bannerList = GsonUtils.fromJson(cache.json, Array<BannerItem>::class.java).toList()
            log("cache = ${bannerList.toJson(format = false).substring(0, 150)}")
            bannerList
        }
    }

    suspend fun getHomeTopList() = withContext(Dispatchers.IO) {
        val apiName = "honeTopList"
        if (NetworkUtils.isAvailable()) {
            val dataXList = apiService.getHomeTopList().apiData()!!
            CacheEntity(json = dataXList.toJson(), apiName = apiName)
                .apply { saveOrUpdate("apiName = ?", apiName) }
            dataXList
        } else {
            // Gson().fromJson() 将 json 转回 list 或 array
            // object : TypeToken<ArrayList<BannerItem>>() {} // List 要用 TypeToken
            // Array<BannerItem>::class.java // Array 可以直接使用对象 class
            val cache = LitePal.where("apiName = ?", apiName)
                .findFirst(CacheEntity::class.java)
            val dataXList = GsonUtils.fromJson(cache.json, Array<DataX>::class.java).toList()
            log("cache = ${dataXList.toJson(format = false).substring(0, 150)}")
            dataXList
        }
    }

    suspend fun getHomeList(page: Int) = withContext(Dispatchers.IO) {
        val apiName = "homeList"
        if (NetworkUtils.isAvailable()) {
            val articles = apiService.getHomeList(page = page).apiData()!!
            CacheEntity(json = articles.toJson(), apiName = apiName, page = "$page")
                .apply { saveOrUpdate("apiName = ? and page = ?", apiName, "$page") }
            articles
        } else {
            val cache = LitePal.where("apiName = ? and page = ?", apiName, "$page")
                .findFirst(CacheEntity::class.java)
            val articles = GsonUtils.fromJson(cache.json, Articles::class.java)
            log("cache = ${articles.toJson(format = false).substring(0, 150)}")
            articles
        }
    }

    suspend fun getProjectTree() = withContext(Dispatchers.IO) {
        apiService.getProjectTree().apiData()!!
    }

    suspend fun getProjectList(id: Int, page: Int) = withContext(Dispatchers.IO) {
        val apiName = "projectList"
        if (NetworkUtils.isAvailable()) {
            val articles = apiService.getProjectList(id = id, page = page).apiData()!!
            CacheEntity(json = articles.toJson(), apiName = apiName, page = "$page", arg1 = "$id")
                .apply {
                    saveOrUpdate("apiName = ? and page = ? and arg1 = ?", apiName, "$page", "$id")
                }
            articles
        } else {
            val cache =
                LitePal.where("apiName = ? and page = ? and arg1 = ?", apiName, "$page", "$id")
                    .findFirst(CacheEntity::class.java)
            val articles = GsonUtils.fromJson(cache.json, Articles::class.java)
            log("cache = ${articles.toJson(format = false).substring(0, 150)}")
            articles
        }
    }

    suspend fun getNewProjectList(page: Int) = withContext(Dispatchers.IO) {
        val apiName = "newProjectList"
        if (NetworkUtils.isAvailable()) {
            val articles = apiService.getNewProjectList(page = page).apiData()!!
            CacheEntity(json = articles.toJson(), apiName = apiName, page = "$page")
                .apply { saveOrUpdate("apiName = ? and page = ?", apiName, "$page") }
            articles
        } else {
            val cache = LitePal.where("apiName = ? and page = ?", apiName, "$page")
                .findFirst(CacheEntity::class.java)
            val articles = GsonUtils.fromJson(cache.json, Articles::class.java)
            log("cache = ${articles.toJson(format = false).substring(0, 150)}")
            articles
        }
    }

    suspend fun getSquareList(page: Int) = withContext(Dispatchers.IO) {
        val apiName = "squareList"
        if (NetworkUtils.isAvailable()) {
            val articles = squareService.getSquareList(page = page).apiData()!!
            CacheEntity(json = articles.toJson(), apiName = apiName, page = "$page")
                .apply { saveOrUpdate("apiName = ? and page = ?", apiName, "$page") }
            articles
        } else {
            val cache = LitePal.where("apiName = ? and page = ?", apiName, "$page")
                .findFirst(CacheEntity::class.java)
            val articles = GsonUtils.fromJson(cache.json, Articles::class.java)
            log("cache = ${articles.toJson(format = false).substring(0, 150)}")
            articles
        }
    }

    suspend fun getWxArticleTree() = withContext(Dispatchers.IO) {
        apiService.getWxArticleTree().apiData()!!
    }

    suspend fun getWxArticleList(id: Int, page: Int) = withContext(Dispatchers.IO) {
        val apiName = "wxArticleList"
        if (NetworkUtils.isAvailable()) {
            val articles = apiService.getWxArticleList(id = id, page = page).apiData()!!
            CacheEntity(json = articles.toJson(), apiName = apiName, page = "$page", arg1 = "$id")
                .apply {
                    saveOrUpdate("apiName = ? and page = ? and arg1 = ?", apiName, "$page", "$id")
                }
            articles
        } else {
            val cache =
                LitePal.where("apiName = ? and page = ? and arg1 = ?", apiName, "$page", "$id")
                    .findFirst(CacheEntity::class.java)
            val articles = GsonUtils.fromJson(cache.json, Articles::class.java)
            log("cache = ${articles.toJson(format = false).substring(0, 150)}")
            articles
        }
    }

    suspend fun likeArticle(id: Int) = withContext(Dispatchers.IO) {
        // 此接口会返回 ”“ Retrofit 解析为 null
        likeService.likeArticle(id).apiData()
    }

    suspend fun unlikeArticle(id: Int) = withContext(Dispatchers.IO) {
        likeService.unlikeArticle(id).apiData()
    }

    suspend fun unlikeMyLike(id: Int, originId: Int) = withContext(Dispatchers.IO) {
        likeService.unlikeMyLike(id = id, originId = originId).apiData()
    }

    suspend fun getLikeList(page: Int) = withContext(Dispatchers.IO) {
        val apiName = "likeList"
        if (NetworkUtils.isAvailable()) {
            val articles = likeService.getLikeList(page).apiData()!!
            CacheEntity(json = articles.toJson(), apiName = apiName, page = "$page")
                .apply {
                    saveOrUpdate("apiName = ? and page = ?", apiName, "$page")
                }
            articles
        } else {
            val cache = LitePal.where("apiName = ? and page = ?", apiName, "$page")
                .findFirst(CacheEntity::class.java)
            val articles = GsonUtils.fromJson(cache.json, Articles::class.java)
            log("cache = ${articles.toJson(format = false).substring(0, 150)}")
            articles
        }
    }

    suspend fun search(page: Int, key: String) = withContext(Dispatchers.IO) {
        apiService.search(page = page, k = key).apiData()!!
    }

    suspend fun getQAList(page: Int) = withContext(Dispatchers.IO) {
        val apiName = "qaList"
        if (NetworkUtils.isAvailable()) {
            val articles = apiService.getQAList(page = page).apiData()!!
            CacheEntity(json = articles.toJson(), apiName = apiName, page = "$page")
                .apply {
                    saveOrUpdate("apiName = ? and page = ?", apiName, "$page")
                }
            articles
        } else {
            val cache = LitePal.where("apiName = ? and page = ?", apiName, "$page")
                .findFirst(CacheEntity::class.java)
            val articles = GsonUtils.fromJson(cache.json, Articles::class.java)
            log("cache = ${articles.toJson(format = false).substring(0, 150)}")
            articles
        }
    }

    suspend fun getQACommentList(id: Int) = withContext(Dispatchers.IO) {
        apiService.getQACommentList(id = id).apiData()!!
    }

}