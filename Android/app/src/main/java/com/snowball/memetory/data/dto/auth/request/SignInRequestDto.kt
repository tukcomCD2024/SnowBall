package com.snowball.memetory.data.dto.auth.request

import java.io.Serializable

data class SignInRequestDto(
    val token: String,
    val socialType: String,
    val fcmToken: String
): Serializable
