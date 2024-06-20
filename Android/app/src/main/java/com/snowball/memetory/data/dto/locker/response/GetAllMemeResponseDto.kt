package com.snowball.memetory.data.dto.locker.response

import com.snowball.memetory.domain.model.locker.Meme

data class GetAllMemeResponseDto(
    val totalPage: Int,
    val currentPage: Int,
    val memeList: List<Meme>
)
