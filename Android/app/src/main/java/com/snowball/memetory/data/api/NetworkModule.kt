package com.snowball.memetory.data.api

import com.snowball.memetory.BuildConfig
import com.snowball.memetory.util.TokenManager
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

    // okHttpClientHeader 인스턴스
    val okHttpClientHeader: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val accessToken = TokenManager.getAccessToken() // 토큰 가져오기

            // 액세스 토큰이 있는 경우, 요청에 헤더 추가
            val newRequest = originalRequest.newBuilder().apply {
                if (accessToken != null) {
                    header("Authorization", "Bearer $accessToken")
                }
            }.build()

            chain.proceed(newRequest)
        }
        .addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
        .build()

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

    // 애플리케이션의 기본 API용 Retrofit 설정 - 헤더에 엑세스토큰 값 포함
    private val retrofitAppHeader = Retrofit.Builder()
        .baseUrl(BASE_URL) // 애플리케이션 서버의 베이스 URL
        .client(okHttpClientHeader)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    ///////////////// 서비스 인터페이스 제공 //////////////////
    // 로그인
    val authService: AuthService = retrofitApp.create(AuthService::class.java)
    val googleAuthService: AuthService = retrofitGoogle.create(AuthService::class.java)
    // 밈 생성
    val generateMemeService: GenerateMemeService = retrofitAppHeader.create(GenerateMemeService::class.java)

}