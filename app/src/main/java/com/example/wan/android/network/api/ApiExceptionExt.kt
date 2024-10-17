package com.example.wan.android.network.api

fun Throwable.isApiException() = this is ApiException

fun Throwable.isNoLogin() = this is ApiException && this.errorCode == -1001
