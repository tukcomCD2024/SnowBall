package com.example.memetory.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
import com.example.memetory.global.security.jwt.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberApi {
	private final MemberService memberService;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;

	@PostMapping("/sign-up")
	@Override
	public ResponseEntity<HttpStatus> register(
		HttpServletResponse response, @RequestBody MemberSignUpRequest memberSignUpRequest,
		@LoginMemberEmail String email) {
		MemberServiceDto memberServiceDto = memberSignUpRequest.toServiceDto(email);

		memberService.register(memberServiceDto);

		String refreshToken = jwtService.createRefreshToken();
		jwtService.setRefreshTokenHeader(response, refreshToken);
		refreshTokenService.updateToken(email, refreshToken);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
