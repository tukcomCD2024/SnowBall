package com.example.memetory.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.exception.NotFoundMemberException;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.exception.NotFoundEmailException;
import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
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
	private final RefreshTokenService refreshTokenService;
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	@Transactional
	public void register(Member member, MemberSignUpRequest memberSignUpRequest) {
		member.register(memberSignUpRequest);

		String refreshToken = jwtService.createRefreshToken();

		jwtService.setRefreshTokenHeader(response, refreshToken);
		refreshTokenService.updateToken(member.getEmail(), refreshToken);
	}

	@Transactional(readOnly = true)
	public Member getLoginMember() {
		String accessToken = jwtService.extractAccessToken(request).orElseThrow(NotFoundTokenException::new);
		String email = jwtService.extractEmail(accessToken).orElseThrow(NotFoundEmailException::new);
		Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

		return member;
	}

	@Transactional(readOnly = true)
	public Member findById(Long id) {
		return memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
	}
}
