package com.example.memetory.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SocialType {
	GOOGLE("https://www.googleapis.com/oauth2/v2/userinfo"),
	KAKAO("https://kapi.kakao.com/v2/user/me");

	private final String providerUrl;

	@JsonCreator
	public static SocialType from(String s) {
		return SocialType.valueOf(s.toUpperCase());
	}
}
