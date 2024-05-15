package com.snowball.memetory.data.dto.generatememe.voice.request

import java.io.Serializable

data class VoiceIdRequestDto(
    val s3Key: String,
    val name: String,
    val description: String
): Serializable
