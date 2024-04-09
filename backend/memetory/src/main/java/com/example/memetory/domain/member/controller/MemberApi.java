package com.example.memetory.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.memetory.domain.member.dto.MemberUpdateDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Member")
public interface MemberApi {
	@Operation(
		summary = "멤버 업데이트",
		description = "멤버의 이미지 url 및 닉네임 업데이트"
			+ "업데이트를 하지 않을 필드의 경우에는, 기존 값을 넣어 주면 된다.",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "이미지 생성 성공"
		),
		@ApiResponse(
			responseCode = "409",
			description = "닉네임 중복"
		)}
	)
	ResponseEntity<HttpStatus> updateMember(@Parameter(hidden = true) String email, MemberUpdateDto memberUpdateDto);
}
