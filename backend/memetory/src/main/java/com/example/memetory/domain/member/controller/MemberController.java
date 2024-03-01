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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final JwtService jwtService;

	@PostMapping("/sign-up")
	public ResponseEntity<HttpStatus> register(@RequestBody MemberSignUpRequest memberSignUpRequest,
		HttpServletRequest request, HttpServletResponse response) {
		memberService.register(request, response, memberSignUpRequest);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
