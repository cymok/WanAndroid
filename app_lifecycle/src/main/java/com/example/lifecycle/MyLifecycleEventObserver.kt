package com.example.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.utils.toast

class MyLifecycleEventObserver : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        toast(event.name)
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

            }

            Lifecycle.Event.ON_START -> {

            }

            Lifecycle.Event.ON_RESUME -> {

            }

            Lifecycle.Event.ON_PAUSE -> {

            }

            Lifecycle.Event.ON_STOP -> {

            }

            Lifecycle.Event.ON_DESTROY -> {

            }

            Lifecycle.Event.ON_ANY -> {

            }
        }
    }
}