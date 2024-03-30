package com.example.memetory.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Schema(description = "회원 가입 포맷")
public class MemberSignUpRequest {

	@Schema(description = "닉네임")
	private String nickName;

	public MemberServiceDto toServiceDto(String email) {
		return MemberServiceDto.builder()
			.email(email)
			.nickname(nickName)
			.build();
	}
}
