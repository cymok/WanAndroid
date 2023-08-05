package com.example.flamingo.network.api

class ApiException(
    var errorCode: Int,
    override var message: String
) : RuntimeException()