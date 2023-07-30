package com.example.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.utils.toast

class MyLifecycleObserver : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        toast("onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        toast("onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        toast("onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        toast("onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        toast("onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        toast("onDestroy")
    }

}