package com.example.flamingo.utils

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.BuildConfig
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import com.umeng.message.api.UPushRegisterCallback

@Suppress("MemberVisibilityCanBePrivate")
object UmengUtils {

    fun preInit(context: Context) {
        UMConfigure.preInit(context, BuildConfig.UMENG_APP_KEY, getChannel())
    }

    fun init(context: Context) {
        UMConfigure.init(context, BuildConfig.UMENG_APP_KEY, getChannel(), UMConfigure.DEVICE_TYPE_PHONE, BuildConfig.UMENG_MESSAGE_SECRET)
        //注册推送
        PushAgent.getInstance(context).register(object : UPushRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                LogUtils.iTag("Umeng", "注册成功 deviceToken:$deviceToken")
            }

            override fun onFailure(errCode: String, errDesc: String) {
                LogUtils.eTag("Umeng", "注册失败 code:$errCode, desc:$errDesc")
            }
        })
    }

    fun getChannel(): String {
        return BuildConfig.APP_CHANNEL
    }

    fun getDeviceToken(context: Context): String {
        return PushAgent.getInstance(context).registrationId
    }

}