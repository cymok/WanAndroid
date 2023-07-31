package com.example.flamingo.service

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService

class MyService : LifecycleService() {

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        TODO("Return the communication channel to the service.")
    }

}