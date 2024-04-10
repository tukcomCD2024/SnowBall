package com.example.memetory.global.security.jwt.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.exception.NotFoundEmailException;
import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;

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

	/**
	 * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
	 * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
	 */
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private static final String EMAIL_CLAIM = "email";
	private static final String BEARER = "Bearer ";

	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;

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

	// 함께 보낼때는 둘 다 create
	public void sendAccessAndRefreshToken(HttpServletResponse response, String email) {
		setTokenHeader(response, accessHeader, createAccessToken(email));

		String refreshToken = createRefreshToken();
		setTokenHeader(response, refreshHeader, refreshToken);
		refreshTokenService.updateToken(email, refreshToken);
		log.info("Access Token, Refresh Token 헤더 설정 완료");
	}

	private String createAccessToken(String email) {
		Date now = new Date();
		return JWT.create() // JWT 토큰을 생성하는 빌더 반환
			.withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
			.withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정
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

	// 헤더에서 RefreshToken 추출
	public Optional<String> extractRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(refreshHeader))
			.filter(refreshToken -> refreshToken.startsWith(BEARER))
			.map(refreshToken -> refreshToken.replace(BEARER, ""));
	}

	// 헤더에서 AccessToken 추출
	public Optional<String> extractAccessToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(accessHeader))
			.filter(refreshToken -> refreshToken.startsWith(BEARER))
			.map(refreshToken -> refreshToken.replace(BEARER, ""));
	}

	/**
	 * AccessToken에서 Email 추출
	 * 추출 전에 JWT.require()로 검증기 생성
	 * verify로 AceessToken 검증 후
	 * 유효하다면 getClaim()으로 이메일 추출
	 * 유효하지 않다면 빈 Optional 객체 반환
	 */
	public Optional<String> extractEmail(String accessToken) {
		try {
			// 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
			return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
				.build() // 반환된 빌더로 JWT verifier 생성
				.verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
				.getClaim(EMAIL_CLAIM) // claim(Emial) 가져오기
				.asString());
		} catch (Exception e) {
			// 예외로 출력 -> NotValidTokenException
			log.error("액세스 토큰이 유효하지 않습니다.");
			return Optional.empty();
		}
	}

	public String getEmail(HttpServletRequest request) {
		String accessToken = this.extractAccessToken(request).orElseThrow(NotFoundTokenException::new);
		return this.extractEmail(accessToken).orElseThrow(NotFoundEmailException::new);
	}

	private void setTokenHeader(HttpServletResponse response, String headerName, String token) {
		response.setHeader(headerName, BEARER + token);
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
