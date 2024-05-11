package com.snowball.memetory.data.api

import com.snowball.memetory.data.dto.auth.request.GoogleTokenRequestDto
import com.snowball.memetory.data.dto.auth.request.SignInRequestDto
import com.snowball.memetory.data.dto.auth.response.GoogleTokenResponseDto
import com.snowball.memetory.data.dto.auth.response.TokenResponseDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/login")
    fun loginUser(@Body request: SignInRequestDto): Call<TokenResponseDto> // 서버의 응답에 따라 반환 타입 수정 필요
//    fun loginUser(@Body request: SignInRequestDto): Call<Response<TokenResponseDto>> // 서버의 응답에 따라 반환 타입 수정 필요

    @POST("/oauth2/v4/token")
    fun requestAccessToken(@Body request: GoogleTokenRequestDto): Call<GoogleTokenResponseDto>
//    fun requestAccessToken(@Body request: GoogleTokenRequestDto): Call<Response<GoogleTokenResponseDto>>
}