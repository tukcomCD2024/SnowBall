package com.example.memetory.domain.auth;

import java.util.Map;

import com.example.memetory.domain.auth.dto.LoginRequest;
import com.example.memetory.domain.member.entity.SocialType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthFixture {
	static ObjectMapper mapper = new ObjectMapper();

	public static String JSON_GOOGLE_LOGIN_REQUEST() throws JsonProcessingException {
		return mapper.writeValueAsString(GOOGLE_LOGIN_REQUEST());
	}

	public static LoginRequest GOOGLE_LOGIN_REQUEST() {
		return new LoginRequest("google login token", SocialType.GOOGLE, "fcmToken");
	}

	public static LoginRequest GOOGLE_LOGIN_REQUEST_OTHER_FCM_TOKE() {
		return new LoginRequest("google login token", SocialType.GOOGLE, "fcmToken");
	}

	public static String JSON_GOOGLE_OAUTH2_RESPONSE() throws JsonProcessingException {
		Map<String, String> googleResponse = Map.ofEntries(
			Map.entry("id", "123456789"),
			Map.entry("name", "이준우"),
			Map.entry("picture", "urlS3"));

		return mapper.writeValueAsString(googleResponse);
	}
}
