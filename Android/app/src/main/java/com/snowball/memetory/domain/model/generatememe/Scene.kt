package com.snowball.memetory.domain.model.generatememe

data class Scene(
    val sourceImage: String,
    val targetImage: String,
    val text: String,
    val voiceId: String
)