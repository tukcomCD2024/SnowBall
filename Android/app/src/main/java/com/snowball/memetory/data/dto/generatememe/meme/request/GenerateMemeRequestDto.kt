package com.snowball.memetory.data.dto.generatememe.meme.request

import com.snowball.memetory.domain.model.generatememe.Scene

data class GenerateMemeRequestDto(
    val scene: List<Scene>
)