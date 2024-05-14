package com.snowball.memetory.data.api

import com.snowball.memetory.data.dto.generatememe.voice.request.VoiceIdRequestDto
import com.snowball.memetory.data.dto.generatememe.voice.response.VoiceIdResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GenerateMemeService {
    @POST("/voice")
    suspend fun extractVoice(@Body voiceInfo: VoiceIdRequestDto): Response<VoiceIdResponseDto>

}