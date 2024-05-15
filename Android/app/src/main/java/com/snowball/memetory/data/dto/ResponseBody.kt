package com.snowball.memetory.data.dto

data class ResponseBody<T>(
    val status: Int,
    val message: String,
    val data: T
)
