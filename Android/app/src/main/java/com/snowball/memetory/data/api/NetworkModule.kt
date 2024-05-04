package com.snowball.memetory.data.api

import com.snowball.memetory.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = BuildConfig.BASE_URL
    private const val GOOGLE_TOKEN_URL = BuildConfig.GOOGLE_API_URL

    // HttpLoggingInterceptor 적용
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 로그에 okhttp.OkHttpClient으로 http 통신간 데이터 주고받는 것들이 나옴
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Google OAuth용 Retrofit 설정
    private val retrofitGoogle = Retrofit.Builder()
        .baseUrl(GOOGLE_TOKEN_URL) // Google API 베이스 URL
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 애플리케이션의 기본 API용 Retrofit 설정
    private val retrofitApp = Retrofit.Builder()
        .baseUrl(BASE_URL) // 애플리케이션 서버의 베이스 URL
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 서비스 인터페이스 제공
    val authService: AuthService = retrofitApp.create(AuthService::class.java)
    val googleAuthService: AuthService = retrofitGoogle.create(AuthService::class.java)

}