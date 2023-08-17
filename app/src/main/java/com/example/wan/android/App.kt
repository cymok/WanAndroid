package com.example.wan.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import coil.Coil
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.example.wan.android.config.CoilConfig
import com.example.wan.android.utils.UmengUtils
import com.example.wan.android.utils.getViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.entity.UMessage
import java.text.SimpleDateFormat

class App : Application() {

    companion object {

        @JvmStatic
        lateinit var INSTANCE: App
            private set

    }

    val appViewModel: AppViewModel by lazy { getViewModel() }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleEventObserver())
        initNightModel()
        initCoil()
        initSmartRefreshLayout()
//        initUmengSDK()
    }

    private fun initUmengSDK() {
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        // 预初始化，不采集设备信息
        UmengUtils.preInit(this)

        umengCustomMessage()
    }

    private fun umengCustomMessage() {
        object : UmengMessageHandler() {
            override fun dealWithCustomMessage(context: Context?, msg: UMessage?) {
                super.dealWithCustomMessage(context, msg)
                try {
                    val json = GsonUtils.toJson(msg)
                    msg?.let {
                        // 自定义消息内容
                        val msgContent = msg.custom
                        // 自定义消息参数
                        val msgExtra = msg.extra
                        // 执行业务
                        // ...
                    }
                } catch (e: Exception) {
                    LogUtils.e(e)
                }
            }
        }.let {
            PushAgent.getInstance(this).messageHandler = it
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            ClassicsHeader(context)
                .setTimeFormat(SimpleDateFormat("上次更新 yyyy/MM/dd HH:mm:ss"))
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context)
        }
    }

    private fun initCoil() {
        Coil.setImageLoader(CoilConfig.getImageLoader(this))
    }

    private fun initNightModel() {
        val lightModel = SPUtils.getInstance().getInt("night_module")
        AppCompatDelegate.setDefaultNightMode(lightModel)
    }

}