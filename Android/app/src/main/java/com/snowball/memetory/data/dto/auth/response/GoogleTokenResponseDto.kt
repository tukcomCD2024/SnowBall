package com.snowball.memetory.data.dto.auth.response

import com.google.gson.annotations.SerializedName

data class GoogleTokenResponseDto(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("expires_in") val expires_in: Int,
    @SerializedName("scope") val scope: String,
    @SerializedName("token_type") val token_type: String,
    @SerializedName("id_token") val id_token: String
)
