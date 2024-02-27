package com.example.memetory.domain.member.service;

import org.springframework.stereotype.Service;

import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.NotFoundMemberException;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.exception.NotFoundEmailException;
import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;
import com.example.memetory.global.security.jwt.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtService jwtService;

	public void register(HttpServletRequest request, MemberSignUpRequest memberSignUpRequest) {
		String accessToken = jwtService.extractAccessToken(request).orElseThrow(NotFoundTokenException::new);
		String email = jwtService.extractEmail(accessToken).orElseThrow(NotFoundEmailException::new);
		Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

		member.register(memberSignUpRequest);
	}
}
