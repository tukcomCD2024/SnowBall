package com.snowball.memetory.data.api

import com.snowball.memetory.data.dto.ResponseBody
import com.snowball.memetory.data.dto.generatememe.voice.request.VoiceIdRequestDto
import com.snowball.memetory.data.dto.generatememe.voice.response.VoiceIdResponseDto
import com.snowball.memetory.domain.model.voice.VoiceList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GenerateMemeService {
    @POST("/voice")
    suspend fun extractVoice(@Body voiceInfo: VoiceIdRequestDto): Response<VoiceIdResponseDto>

    @GET("/voice/library") // 일레븐랩스 기본 목소리 가져오기
    suspend fun getVoiceIdList(): Response<ResponseBody<VoiceList>>

    //    @GET("/voice/member") 단일 조회
//    suspend fun getVoiceId(): Response<ResponseBody<VoiceList>>


}