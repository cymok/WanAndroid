package com.example.wan.android

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import com.blankj.utilcode.util.SPUtils
import com.example.wan.android.utils.getViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
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
        initSmartRefreshLayout()
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
        val lightModel = SPUtils.getInstance().getInt("night_module")
        AppCompatDelegate.setDefaultNightMode(lightModel)
    }

}