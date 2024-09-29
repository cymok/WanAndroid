package com.example.wan.android.utils

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.example.wan.android.data.SupperUserInfo

object UserUtils {
    private val spUtils = SPUtils.getInstance("UserInfo")
    private const val ENCRYPT_KEY = "UserInfo"

    fun saveSupperUserInfo(supperUserInfo: SupperUserInfo) {
        val json = supperUserInfo.toJson() ?: ""
        val encryption = json.encrypt(ENCRYPT_KEY)
        spUtils.put("SupperUserInfo", encryption)
    }

    fun getSupperUserInfo(): SupperUserInfo? {
        val encryption = spUtils.getString("SupperUserInfo")
        val json = encryption.decrypt(ENCRYPT_KEY)
        return try {
            GsonUtils.fromJson(json, SupperUserInfo::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        spUtils.remove("UserInfo")
        spUtils.remove("SupperUserInfo")
    }

    val isLogin get() = getSupperUserInfo()?.userInfo != null && getSupperUserInfo()?.userInfo?.id != null

}