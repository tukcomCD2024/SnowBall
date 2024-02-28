package com.example.memetory.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.security.jwt.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final JwtService jwtService;

	@PostMapping("/sign-up")
	public ResponseEntity<HttpStatus> register(@RequestBody MemberSignUpRequest memberSignUpRequest,
		HttpServletRequest request) {
		memberService.register(request, memberSignUpRequest);

		return ResponseEntity.status(HttpStatus.OK).build();

	}
}
