package com.example.flamingo

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import coil.Coil
import com.example.flamingo.config.CoilConfig
import com.example.flamingo.utils.UmengUtils
import com.example.flamingo.utils.getViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.umeng.commonsdk.UMConfigure
import org.android.agoo.honor.HonorRegister
import org.android.agoo.huawei.HuaWeiRegister
import org.android.agoo.mezu.MeizuRegister
import org.android.agoo.oppo.OppoRegister
import org.android.agoo.vivo.VivoRegister
import org.android.agoo.xiaomi.MiPushRegistar
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
        initCoil()
        initSmartRefreshLayout()
        initUmengSDK()
    }

    private fun initUmengSDK() {
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        // 预初始化，不采集设备信息
        UmengUtils.preInit(this)
        // 配置厂商通道
        MiPushRegistar.register(this, BuildConfig.XIAOMI_APP_ID, BuildConfig.XIAOMI_APP_KEY, false) // 日志过滤 tag：MiPushBroadcastReceiver
        HuaWeiRegister.register(this) // 日志过滤：HuaWeiRegister
        OppoRegister.register(this, BuildConfig.OPPO_APP_KEY, BuildConfig.OPPO_MASTER_SECRET) // 日志过滤 tag：OppoPush
        VivoRegister.register(this) // 日志过滤 tag：PushMessageReceiver
        HonorRegister.register(this) // 日志过滤：HonorRegister
        MeizuRegister.register(this, BuildConfig.MEIZU_APP_ID, BuildConfig.MEIZU_APP_KEY) // 日志过滤 tag：MeizuPushReceiver
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

}