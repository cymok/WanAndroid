package com.example.wan.android.utils

import retrofit2.Retrofit

inline fun <reified T> Retrofit.create() {
    this.create(T::class.java)
}
