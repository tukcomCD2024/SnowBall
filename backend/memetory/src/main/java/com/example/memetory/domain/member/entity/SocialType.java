package com.example.memetory.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SocialType {
	GOOGLE(),
	KAKAO();

	@JsonCreator
	public static SocialType from(String s) {
		return SocialType.valueOf(s.toUpperCase());
	}
}
