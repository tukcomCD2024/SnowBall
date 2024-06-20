package com.snowball.memetory.data.repository

import com.snowball.memetory.data.api.AuthService
import com.snowball.memetory.data.dto.auth.request.GoogleTokenRequestDto
import com.snowball.memetory.data.dto.auth.request.SignInRequestDto
import com.snowball.memetory.data.dto.auth.response.GoogleTokenResponseDto
import com.snowball.memetory.data.dto.auth.response.TokenResponseDto
import retrofit2.awaitResponse

class AuthRepository(private val authService: AuthService, private val googleAuthService: AuthService) {
    suspend fun requestAccessToken(request: GoogleTokenRequestDto): Result<GoogleTokenResponseDto> {
        return try {
            val response = googleAuthService.requestAccessToken(request).awaitResponse()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Failed to fetch token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(request: SignInRequestDto): Result<TokenResponseDto> {
        return try {
            val response = authService.loginUser(request).awaitResponse()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Login Failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

