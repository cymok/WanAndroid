package com.example.flamingo

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import coil.Coil
import com.example.flamingo.config.CoilConfig

class App : Application() {

    companion object {

        @JvmStatic
        lateinit var INSTANCE: App
            private set

    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleEventObserver())
        initCoil()
    }

    private fun initCoil() {
        Coil.setImageLoader(CoilConfig.getImageLoader(this))
    }

}