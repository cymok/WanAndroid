package com.example.wan.android

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import coil.Coil
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.SPUtils
import com.example.wan.android.config.CoilConfig
import com.example.wan.android.constant.AppConst
import com.example.wan.android.utils.getViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.litepal.LitePal
import java.text.SimpleDateFormat

class App : MultiDexApplication() {

    companion object {

        @JvmStatic
        lateinit var INSTANCE: App
            private set

        var launchTime = 0L
        var appCreateTime = 0L
        var splashCreateTime = 0L
        var mainCreateTime = 0L

    }

    val appViewModel: AppViewModel by lazy { getViewModel() }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        launchTime = System.currentTimeMillis()
    }

    override fun onCreate() {
        super.onCreate()
        appCreateTime = System.currentTimeMillis()
        INSTANCE = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleEventObserver())
        initNightModel()
        initSmartRefreshLayout()
        initLitePal()
        initCoil() // compose
        CrashUtils.init(AppConst.crashPath)
    }

    private fun initCoil() {
        Coil.setImageLoader(CoilConfig.getImageLoader(this))
    }

    private fun initLitePal() {
        LitePal.initialize(this)
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

    private fun initNightModel() {
        val lightModel = SPUtils.getInstance().getInt("night_model")
        AppCompatDelegate.setDefaultNightMode(lightModel)
    }

}