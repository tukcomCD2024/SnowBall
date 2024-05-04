package com.snowball.memetory.presentation.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowball.memetory.data.dto.auth.request.GoogleTokenRequestDto
import com.snowball.memetory.data.dto.auth.request.SignInRequestDto
import com.snowball.memetory.data.dto.auth.response.GoogleTokenResponseDto
import com.snowball.memetory.data.dto.auth.response.TokenResponseDto
import com.snowball.memetory.data.repository.AuthRepository
import kotlinx.coroutines.launch


class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _accessTokenResult = MutableLiveData<Result<GoogleTokenResponseDto>>()
    val accessTokenResult: LiveData<Result<GoogleTokenResponseDto>> = _accessTokenResult

    private val _loginResult = MutableLiveData<Result<TokenResponseDto>>()
    val loginResult: LiveData<Result<TokenResponseDto>> = _loginResult

//    fun requestAccessToken(request: GoogleTokenRequestDto) {
//        viewModelScope.launch {
//            val result = authRepository.requestAccessToken(request)
//            // 이제 GoogleTokenResponseDto 결과를 accessTokenResult로 포스팅
//            _accessTokenResult.postValue(result)
//        }
//    }

    fun requestAccessToken(request: GoogleTokenRequestDto) {
        viewModelScope.launch {
            val result = authRepository.requestAccessToken(request)

            result.onSuccess { googleTokenResponse ->
                _accessTokenResult.postValue(Result.success(googleTokenResponse))
                loginUser(SignInRequestDto(googleTokenResponse.access_token, "GOOGLE")) // Assuming serverAuthCode is a property
            }
            result.onFailure {
                _accessTokenResult.postValue(Result.failure(it))
            }
        }
//        viewModelScope.launch {
//            val result = authRepository.requestAccessToken(request)
//            result.onSuccess {
//                _accessTokenResult.postValue(Result.success(it))
//
//            }
//            result.onFailure {
//                _accessTokenResult.postValue(Result.failure(it))
//            }
//        }
    }

    fun loginUser(request: SignInRequestDto) {
        viewModelScope.launch {
            val result = authRepository.loginUser(request)
            _loginResult.postValue(result)
        }
    }
}

//class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
//    fun login(signInRequestDto: SignInRequestDto) {
//        authRepository.loginUser(signInRequestDto, object : AuthRepository.AuthCallback {
//            override fun onSuccess(authToken: String?, refreshToken: String?, tokenResponse: TokenResponseDto?) {
//                Log.d("AUTH", "authToken = $authToken, refreshToken = $refreshToken, tokenResponse = $tokenResponse")
//                // Handle success
//            }
//
//            override fun onError(code: Int?, message: String?) {
//                Log.e("AUTH", "StatusCode = $code, message = $message")
//                // Handle error
//            }
//        })
//    }
//
//    fun requestAccessToken(googleTokenRequestDto: GoogleTokenRequestDto) {
//        authRepository.requestAccessToken(googleTokenRequestDto)
//    }
//
//}