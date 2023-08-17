package com.example.wan.android.network.api

class ApiException(
    var errorCode: Int,
    override var message: String
) : RuntimeException()