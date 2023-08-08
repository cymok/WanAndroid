package com.example.flamingo.utils

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.example.flamingo.data.UserInfo

object UserUtils {
    private val spUtils = SPUtils.getInstance("UserInfo")
    private const val SP_KEY = "UserInfo"
    private const val ENCRYPT_KEY = "UserInfo"

    fun saveUserInfo(userInfo: UserInfo) {
        val json = userInfo.toJson()
        val encryption = json.encrypt(ENCRYPT_KEY)
        spUtils.put(SP_KEY, encryption)
    }

    fun getUserInfo(): UserInfo? {
        val encryption = spUtils.getString(SP_KEY)
        val json = encryption.decrypt(ENCRYPT_KEY)
        return try {
            GsonUtils.fromJson(json, UserInfo::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        spUtils.remove(SP_KEY)
    }

    val isLogin get() = getUserInfo() != null && getUserInfo()?.id != null

}