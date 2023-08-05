package com.example.flamingo.network.repository

import com.example.flamingo.network.ServiceCreator
import com.example.flamingo.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 单一数据源 给 ViewModel 提供数据
 */
object WanRepository {

    private val apiService by lazy { ServiceCreator.wanApiService }

    suspend fun register(username: String, password: String) = withContext(Dispatchers.IO) {
        apiService.register(username, password, password).apiData()
    }

    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        apiService.login(username, password).apiData()
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        apiService.logout().apiData()
    }

    suspend fun getHomeList(page: Int) = withContext(Dispatchers.IO) {
        apiService.getHomeList(page = page).apiData()
    }

    suspend fun getProjectTree() = withContext(Dispatchers.IO) {
        apiService.getProjectTree().apiData()
    }

    suspend fun getProjectList(id: Int, page: Int) = withContext(Dispatchers.IO) {
        apiService.getProjectList(id = id, page = page).apiData() // 莫名错误
    }

    suspend fun getSquareList(page: Int) = withContext(Dispatchers.IO) {
        apiService.getSquareList(page = page).apiData()
    }

    suspend fun getWxArticleTree() = withContext(Dispatchers.IO) {
        apiService.getWxArticleTree().apiData()
    }

    suspend fun getWxArticleList(id: Int, page: Int) = withContext(Dispatchers.IO) {
        apiService.getWxArticleList(id = id, page = page).apiData()
    }

}