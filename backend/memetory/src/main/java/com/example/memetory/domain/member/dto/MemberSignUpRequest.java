package com.example.memetory.domain.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberSignUpRequest {
	private String nickName;

	public MemberServiceDto toServiceDto(String email) {
		return MemberServiceDto.builder()
			.email(email)
			.nickname(nickName)
			.build();
	}
}
