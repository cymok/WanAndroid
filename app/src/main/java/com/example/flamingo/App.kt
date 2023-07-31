package com.example.flamingo

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import coil.Coil
import com.example.flamingo.config.CoilConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleEventObserver())
        initCoil()
    }

    private fun initCoil() {
        Coil.setImageLoader(CoilConfig.getImageLoader(this))
    }

}