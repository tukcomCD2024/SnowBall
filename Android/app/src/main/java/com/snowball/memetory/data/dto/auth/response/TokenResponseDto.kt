package com.snowball.memetory.data.dto.auth.response

import com.google.gson.annotations.SerializedName

data class TokenResponseDto(
    @SerializedName("accessToken")val accessToken: String, // 액세스 토큰
    @SerializedName("refreshToken")val refreshToken: String, // 리프레시 토큰
)