package com.example.memetory.domain.auth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.memetory.domain.auth.dto.LoginRequest;
import com.example.memetory.domain.auth.userInfo.GoogleOAuth2UserInfo;
import com.example.memetory.domain.auth.userInfo.KakaoOAuth2UserInfo;
import com.example.memetory.domain.auth.userInfo.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2ProviderService {

	@Value("${spring.oauth2.google-url}")
	private String googleUrl;
	@Value("${spring.oauth2.kakao-url}")
	private String kakaoUrl;

	public OAuth2UserInfo getUserInfo(LoginRequest request) {
		return switch (request.getSocialType()) {
			case GOOGLE -> getGoogleUserInfo(request);
			case KAKAO -> getKakaoUserInfo(request);
		};
	}

	private OAuth2UserInfo getKakaoUserInfo(LoginRequest request) {
		Map attributes = WebClient.create(kakaoUrl)
			.get()
			.headers(httpHeaders -> {
				httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
				httpHeaders.setBearerAuth(request.getToken());
			})
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(Map.class)
			.log()
			.block();

		return new KakaoOAuth2UserInfo(attributes);
	}

	private OAuth2UserInfo getGoogleUserInfo(LoginRequest request) {
		Map attributes = WebClient.create(googleUrl)
			.get()
			.headers(httpHeaders -> {
				httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
				httpHeaders.setBearerAuth(request.getToken());
			})
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(Map.class)
			.log()
			.block();

		return new GoogleOAuth2UserInfo(attributes);
	}
}

