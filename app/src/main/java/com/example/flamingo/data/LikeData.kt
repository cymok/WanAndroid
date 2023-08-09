package com.example.flamingo.data

data class LikeData(
    val id: Int,
    var like: Boolean,
    val position: Int,
    val position2: Int? = null,
)
