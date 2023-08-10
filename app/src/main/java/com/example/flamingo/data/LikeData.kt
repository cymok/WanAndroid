package com.example.flamingo.data

data class LikeData(
    val id: Int,
    val originId: Int = -1,
    var like: Boolean,
    val position: Int,
    val position2: Int? = null,
)
