package com.example.memetory.domain.auth.dto;

import com.example.memetory.domain.member.entity.SocialType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "로그인 포맷")
public class LoginRequest {
	@Schema(description = "인증서버에서 받아온 access token을 입력")
	private String token;
	@Schema(description = "인증서버를 소문자로 작성, 현재는 google만 가능")
	private SocialType socialType;
}
