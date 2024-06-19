package com.snowball.memetory.domain.model.locker

data class Meme(
    val memeId: Int,
    val s3Url: String,
    val createAt: String,
    val updateAt: String
)
