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
import com.snowball.memetory.util.MyFirebaseMessagingService
import com.snowball.memetory.util.TokenManager
import kotlinx.coroutines.launch


class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _accessTokenResult = MutableLiveData<Result<GoogleTokenResponseDto>>()
    val accessTokenResult: LiveData<Result<GoogleTokenResponseDto>> = _accessTokenResult

    private val _loginResult = MutableLiveData<Result<TokenResponseDto>>()
    val loginResult: LiveData<Result<TokenResponseDto>> = _loginResult

    private val myFirebaseMessagingService = MyFirebaseMessagingService()
    fun requestAccessToken(request: GoogleTokenRequestDto) {
        viewModelScope.launch {
            val result = authRepository.requestAccessToken(request)

            result.onSuccess { googleTokenResponse ->
                _accessTokenResult.postValue(Result.success(googleTokenResponse))
                loginUser(SignInRequestDto(googleTokenResponse.access_token, "GOOGLE", myFirebaseMessagingService.getFirebaseToken())) // Assuming serverAuthCode is a property
                Log.d("AuthViewModel", "$result, $accessTokenResult, ${Result.success(googleTokenResponse)}")
            }
            result.onFailure {
                _accessTokenResult.postValue(Result.failure(it))
            }
        }
    }

    fun loginUser(request: SignInRequestDto) {
        viewModelScope.launch {
            val result = authRepository.loginUser(request)
            _loginResult.postValue(result)
            Log.d("AuthViewModel", "$result, $loginResult")
            Log.d("AuthViewModel", "FCMToken: ${TokenManager.getFCMToken()}")

        }
    }
}