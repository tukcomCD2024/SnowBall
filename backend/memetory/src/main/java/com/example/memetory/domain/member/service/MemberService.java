package com.example.memetory.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.NotFoundMemberException;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.exception.NotFoundEmailException;
import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;
import com.example.memetory.global.security.jwt.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtService jwtService;

	@Transactional
	public void register(HttpServletRequest request, HttpServletResponse response,
		MemberSignUpRequest memberSignUpRequest) {
		String accessToken = jwtService.extractAccessToken(request).orElseThrow(NotFoundTokenException::new);
		String email = jwtService.extractEmail(accessToken).orElseThrow(NotFoundEmailException::new);
		Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

		jwtService.setRefreshTokenHeader(response, jwtService.createRefreshToken());

		member.register(memberSignUpRequest);
	}
}
