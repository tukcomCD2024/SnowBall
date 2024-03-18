package com.example.memetory.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.annotation.LoginMemberEmail;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/sign-up")
	public ResponseEntity<HttpStatus> register(@RequestBody MemberSignUpRequest memberSignUpRequest,
		@LoginMemberEmail String email) {
		memberService.register(email, memberSignUpRequest);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
