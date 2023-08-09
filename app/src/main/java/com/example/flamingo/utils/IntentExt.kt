package com.example.flamingo.utils

import android.content.Context
import android.content.Intent

inline fun <reified T> Context.newIntent(block: Intent.() -> Unit) {
    Intent(this, T::class.java).apply(block)
}
