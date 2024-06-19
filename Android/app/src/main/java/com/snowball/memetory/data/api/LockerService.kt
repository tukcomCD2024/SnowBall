package com.snowball.memetory.data.api

import com.snowball.memetory.data.dto.ResponseBody
import com.snowball.memetory.data.dto.locker.response.GetAllMemeResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LockerService {
    // 밈 전체 조회
    @GET("/meme")
    suspend fun getAllMeme(@Query("page") page: Int, @Query("size") size: Int): Response<ResponseBody<GetAllMemeResponseDto>>

}