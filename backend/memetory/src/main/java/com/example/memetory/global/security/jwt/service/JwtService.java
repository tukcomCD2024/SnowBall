package com.example.memetory.global.security.jwt.service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.dto.TokenResponse;
import com.example.memetory.global.security.jwt.exception.NotFoundEmailException;
import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private static final String EMAIL_CLAIM = "email";
	private static final String BEARER = "Bearer ";

	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;
	private final ObjectMapper objectMapper;

	@Value("${jwt.secretKey}")
	private String secretKey;
	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;
	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;
	@Value("${jwt.access.header}")
	private String accessHeader;
	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	public void sendAccessAndRefreshToken(HttpServletResponse response, String email) {
		try {

		String refreshToken = createRefreshToken();
		String token = objectMapper.writeValueAsString(TokenResponse.builder()
			.accessToken(createAccessToken(email))
			.refreshToken(refreshToken)
			.build());

		response.getWriter().write(token);
		refreshTokenService.updateToken(email, refreshToken);
		} catch (IOException e) {
			// 해당 에러처리 부분에 대해서 고민 필요
			throw new RuntimeException(e);
		}

	}

	private String createAccessToken(String email) {
		Date now = new Date();
		return JWT.create()
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
			.withClaim(EMAIL_CLAIM, email)
			.sign(Algorithm.HMAC512(secretKey));
	}

	private String createRefreshToken() {
		Date now = new Date();
		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
			.sign(Algorithm.HMAC512(secretKey));
	}

	public Optional<String> extractRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(refreshHeader));
	}

	public Optional<String> extractAccessToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(accessHeader));
	}

	public Optional<String> extractEmail(String accessToken) throws JWTVerificationException {
		try {
			return Optional.ofNullable(
				JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken).getClaim(EMAIL_CLAIM).asString());
		} catch (Exception e) {
			log.error("액세스 토큰이 유효하지 않습니다.");
			return Optional.empty();
		}
	}

	public String getEmail(HttpServletRequest request) {
		String accessToken = this.extractAccessToken(request).orElseThrow(NotFoundTokenException::new);
		return this.extractEmail(accessToken).orElseThrow(NotFoundEmailException::new);
	}

	public boolean isTokenValid(String token) {
		try {
			JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
			return true;
		} catch (Exception e) {
			log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
			return false;
		}
	}
}
