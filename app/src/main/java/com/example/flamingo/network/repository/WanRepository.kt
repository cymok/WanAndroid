package com.example.flamingo.network.repository

import com.example.flamingo.network.ServiceCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WanRepository {

    private val apiService by lazy { ServiceCreator.wanApiService }

    suspend fun register(username: String, password: String) = withContext(Dispatchers.IO) {
        val params = mutableMapOf<String, Any>(
            "username" to username,
            "password" to password,
            "repassword" to password,
        )
        apiService.register(params).apiData()
    }

    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        apiService.login(username, password).apiData()
    }

    suspend fun getSquareList(page: Int) = withContext(Dispatchers.IO) {
        apiService.getSquareList(page).apiData()
    }

}