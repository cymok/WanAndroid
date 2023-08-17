package com.example.wan.android.utils

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.example.wan.android.BuildConfig
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import com.umeng.message.api.UPushRegisterCallback
import org.android.agoo.honor.HonorRegister
import org.android.agoo.huawei.HuaWeiRegister
import org.android.agoo.mezu.MeizuRegister
import org.android.agoo.oppo.OppoRegister
import org.android.agoo.vivo.VivoRegister
import org.android.agoo.xiaomi.MiPushRegistar

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
        // 配置厂商通道
        initVendor(context)
    }

    private fun initVendor(context: Context) {
        MiPushRegistar.register(context, BuildConfig.XIAOMI_APP_ID, BuildConfig.XIAOMI_APP_KEY, false) // 日志过滤 tag：MiPushBroadcastReceiver
        HuaWeiRegister.register(context) // 日志过滤：HuaWeiRegister
        OppoRegister.register(context, BuildConfig.OPPO_APP_KEY, BuildConfig.OPPO_MASTER_SECRET) // 日志过滤 tag：OppoPush
        VivoRegister.register(context) // 日志过滤 tag：PushMessageReceiver
        HonorRegister.register(context) // 日志过滤：HonorRegister
        MeizuRegister.register(context, BuildConfig.MEIZU_APP_ID, BuildConfig.MEIZU_APP_KEY) // 日志过滤 tag：MeizuPushReceiver
    }

    fun getChannel(): String {
        return BuildConfig.APP_CHANNEL
    }

    fun getDeviceToken(context: Context): String {
        return PushAgent.getInstance(context).registrationId
    }

}