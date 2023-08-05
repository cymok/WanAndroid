package com.example.flamingo.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
//
//inline fun <reified V : ViewBinding> Fragment.viewBindingInflate() {
//    viewLifecycleOwner.lifecycle.addObserver(VObserver{})
//}

class VObserver : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {

    }
}


interface ViewBindingProperty<in R : Any, out T : ViewBinding> : ReadOnlyProperty<R, T> {

    @MainThread
    fun clear()
}
