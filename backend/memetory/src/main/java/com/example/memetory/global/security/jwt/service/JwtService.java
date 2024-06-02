package com.example.memetory.global.security.jwt.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.dto.TokenResponse;
import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
import com.example.memetory.global.security.jwt.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {
	private static final String BEARER = "Bearer ";

	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;
	private final ObjectMapper objectMapper;
	private final JwtUtil jwtUtil;

	@Value("${jwt.access.header}")
	private String accessHeader;
	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	public void sendAccessAndRefreshToken(HttpServletResponse response, String email) {
		try {
			String refreshToken = jwtUtil.generateRefreshToken();
			String accessToken = jwtUtil.generateAccessToken(email);

			String token = objectMapper.writeValueAsString(TokenResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build());

			response.getWriter().write(token);
			refreshTokenService.updateToken(email, refreshToken);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public String extractRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(refreshHeader))
			.map(token -> token.replace(BEARER, ""))
			.orElseThrow(NotFoundTokenException::new);
	}

	public String extractAccessToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(accessHeader))
			.map(token -> token.replace(BEARER, ""))
			.orElseThrow(NotFoundTokenException::new);
	}

	public String getEmail(HttpServletRequest request) {
		String accessToken = this.extractAccessToken(request);
		return this.extractEmail(accessToken);
	}

	public String extractEmail(String accessToken) {
		return jwtUtil.extractEmailFromAccessToken(accessToken);
	}

	public boolean isTokenValid(String token) {
		return jwtUtil.isTokenValid(token);
	}
}
