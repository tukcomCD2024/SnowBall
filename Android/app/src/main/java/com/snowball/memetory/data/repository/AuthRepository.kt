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

//class AuthRepository(private val authService: AuthService) {
//
//    interface AuthCallback {
//        fun onSuccess(authToken: String?, refreshToken: String?, tokenResponse: TokenResponseDto?)
//        fun onError(code: Int?, message: String?)
//    }
//
//    interface GoogleAccessCallback {
//        fun onSuccess(authToken: String?, refreshToken: String?, tokenResponse: GoogleTokenResponseDto?)
//        fun onError(code: Int?, message: String?)
//    }
//
//    fun loginUser(signInRequestDto: SignInRequestDto, callback: AuthCallback) {
//        Log.e("Auth", "$signInRequestDto")
//        authService.loginUser(signInRequestDto).enqueue(object : retrofit2.Callback<Response<TokenResponseDto>> {
//            override fun onResponse(
//                call: Call<Response<TokenResponseDto>>,
//                response: Response<Response<TokenResponseDto>>
//            ) {
//                Log.e("Auth", "$response, ${response.body()}, ${response.headers()["Authorization"]}")
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    val authToken = response.headers()["Authorization"]
//                    val refreshToken = response.headers()["Authorization-refresh"]
//                    callback.onSuccess(authToken, refreshToken, body?.body())
//                } else {
//
//                    callback.onError(response.code(), response.errorBody()?.string())
//                }
//            }
//
//            override fun onFailure(call: retrofit2.Call<Response<TokenResponseDto>>, t: Throwable) {
//                callback.onError(null, t.message)
//            }
//        })
//    }
//
//    fun requestAccessToken(googleTokenRequestDto: GoogleTokenRequestDto) {
//        authService.requestAccessToken(googleTokenRequestDto).enqueue(object : Callback<GoogleTokenResponseDto> {
//            override fun onResponse(call: Call<GoogleTokenResponseDto>, response: Response<GoogleTokenResponseDto>) {
//                if (response.isSuccessful) {
//                    val tokenResponse = response.body() // Already a GoogleTokenResponseDto object
//                    Log.d("AuthService", "Access Token: ${tokenResponse?.access_token}")
//                    Log.d("AuthService", "ID Token: ${tokenResponse?.id_token}")
//                    Log.d("AuthService", "Token Type: ${tokenResponse?.token_type}")
//                    Log.d("AuthService", "Expires In: ${tokenResponse?.expires_in}")
//                    Log.d("AuthService", "Scope: ${tokenResponse?.scope}")
//
//                } else {
//                    Log.e("AuthService", "Response Error: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<GoogleTokenResponseDto>, t: Throwable) {
//                Log.e("AuthService", "Network error: ${t.message}")
//            }
//        })
//    }

    // ----------------------------------------
//    fun requestAccessToken(googleTokenRequestDto: GoogleTokenRequestDto) {
//        authService.requestAccessToken(googleTokenRequestDto).enqueue(object : retrofit2.Callback<Response<GoogleTokenResponseDto>> {
//
//            override fun onResponse(
//                call: Call<Response<GoogleTokenResponseDto>>,
//                response: Response<Response<GoogleTokenResponseDto>>
//            ) {
//
//                if (response.isSuccessful) {
//                    val body = response.body()?.body()
//                    Log.d("AuthService", "Status Code: ${response.code()}, Success: ${response.isSuccessful}, Body Exists: ${body != null}")
//                    Log.e("AuthService", "${response.headers()}")
//                    if (body != null) {
//                        Log.d("AuthService", "Access Token: ${body.access_token}, ID Token: ${body.id_token}")
//                    }
//                } else {
//                    Log.e("AuthService", "Response Error, Status Code: ${response.code()}, Error Body: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<Response<GoogleTokenResponseDto>>, t: Throwable) {
//                Log.e("AuthService", "Network error: ${t.message}")
//            }
//        })
//    }

//    fun requestAccessToken(googleTokenRequestDto: GoogleTokenRequestDto) {
//        Log.e("Auth", "$googleTokenRequestDto")
//        authService.requestAccessToken(googleTokenRequestDto).enqueue(object : retrofit2.Callback<Response<GoogleTokenResponseDto>> {
//            override fun onResponse(
//                call: Call<Response<GoogleTokenResponseDto>>,
//                response: Response<Response<GoogleTokenResponseDto>>
//            ) {
//
//                Log.e("Auth", "$response")
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        Log.d("AuthService", "Token Response: ${response.body()}")
//                    } else {
//                        Log.d("AuthService", "Response body is null")
//                    }
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("AuthService", "Error Code: ${response.code()} Error Body: $errorBody")
//                }
//            }
//
//            override fun onFailure(call: Call<Response<GoogleTokenResponseDto>>, t: Throwable) {
//                Log.e("AuthService", "Network error: ${t.message}")
//            }
//        })
//    }


//}
