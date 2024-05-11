package com.snowball.memetory.data.dto.auth.request

import java.io.Serializable

data class GoogleTokenRequestDto (
    val grant_type: String,
    val client_id: String,
    val client_secret: String,
    val redirect_uri: String,
    val code: String
): Serializable