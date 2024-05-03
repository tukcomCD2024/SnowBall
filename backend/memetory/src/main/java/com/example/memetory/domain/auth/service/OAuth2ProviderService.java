package com.example.memetory.domain.auth.service;

import static com.example.memetory.domain.member.entity.SocialType.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(OAuth2ProviderService.class);

	public OAuth2UserInfo getUserInfo(LoginRequest request) {
		return switch (request.getSocialType()) {
			case GOOGLE -> getGoogleUserInfo(request);
			case KAKAO -> getKakaoUserInfo(request);
		};
	}

	private OAuth2UserInfo getKakaoUserInfo(LoginRequest request) {
		Map attributes = WebClient.create(KAKAO.getProviderUrl())
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
		Map attributes = WebClient.create(GOOGLE.getProviderUrl())
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

