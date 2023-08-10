package com.example.flamingo.network.repository

import com.example.flamingo.network.ServiceCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        apiService.getBanner().apiData()!!
    }

    suspend fun getHomeTopList() = withContext(Dispatchers.IO) {
        apiService.getHomeTopList().apiData()!!
    }

    suspend fun getHomeList(page: Int) = withContext(Dispatchers.IO) {
        apiService.getHomeList(page = page).apiData()!!
    }

    suspend fun getProjectTree() = withContext(Dispatchers.IO) {
        apiService.getProjectTree().apiData()!!
    }

    suspend fun getProjectList(id: Int, page: Int) = withContext(Dispatchers.IO) {
        apiService.getProjectList(id = id, page = page).apiData()!!
    }

    suspend fun getSquareList(page: Int) = withContext(Dispatchers.IO) {
        squareService.getSquareList(page = page).apiData()!!
    }

    suspend fun getWxArticleTree() = withContext(Dispatchers.IO) {
        apiService.getWxArticleTree().apiData()!!
    }

    suspend fun getWxArticleList(id: Int, page: Int) = withContext(Dispatchers.IO) {
        apiService.getWxArticleList(id = id, page = page).apiData()!!
    }

    suspend fun likeArticle(id: Int) = withContext(Dispatchers.IO) {
        // 此接口会返回 ”“ Retrofit 解析为 null
        likeService.likeArticle(id).apiData()
    }

    suspend fun unlikeArticle(id: Int) = withContext(Dispatchers.IO) {
        likeService.unlikeArticle(id).apiData()
    }

    suspend fun unlikeMyLike(id: Int, originId: Int) = withContext(Dispatchers.IO) {
        likeService.unlikeMyLike(id = id, originId = originId ).apiData()
    }

    suspend fun getLikeList(page: Int) = withContext(Dispatchers.IO) {
        likeService.getLikeList(page).apiData()!!
    }

}