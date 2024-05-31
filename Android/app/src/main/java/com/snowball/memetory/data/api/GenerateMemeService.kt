package com.snowball.memetory.data.api

import com.snowball.memetory.data.dto.ResponseBody
import com.snowball.memetory.data.dto.generatememe.meme.request.GenerateMemeRequestDto
import com.snowball.memetory.data.dto.generatememe.voice.request.VoiceIdRequestDto
import com.snowball.memetory.data.dto.generatememe.voice.response.VoiceIdResponseDto
import com.snowball.memetory.domain.model.voice.Voice
import com.snowball.memetory.domain.model.voice.VoiceList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GenerateMemeService {
    @POST("/voice")
    suspend fun extractVoice(@Body voiceInfo: VoiceIdRequestDto): Response<VoiceIdResponseDto>
//    @GET("/voice/library")
    @GET("/voice/member") // 일레븐랩스 기본 목소리 가져오기 -> 지금 사용자가 추가한 목소리 파일로 바뀐거임
    // 나중에 일레븐랩스 기본 목소리를 가져올 땐 voice/library 사용 및 목소리 리스트로 가져와야함.
    suspend fun getVoiceIdList(): Response<ResponseBody<Voice>>

    //    @GET("/voice/member") 단일 조회
//    suspend fun getVoiceId(): Response<ResponseBody<VoiceList>>

    @POST("/meme")
    suspend fun generateMeme(@Body generateMemeRequest: GenerateMemeRequestDto): Response<ResponseBody<Any>>

}