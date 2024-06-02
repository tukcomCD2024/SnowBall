package com.example.memetory.global.security.jwt.util;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.memetory.global.security.jwt.exception.NotFoundEmailException;

@Component
public class JwtUtil {
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private static final String EMAIL_CLAIM = "email";

	@Value("${jwt.secretKey}")
	private String secretKey;
	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;
	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	public String generateAccessToken(String email) {
		Date now = new Date();
		return JWT.create()
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
			.withClaim(EMAIL_CLAIM, email)
			.sign(Algorithm.HMAC512(secretKey));
	}

	public String generateRefreshToken() {
		Date now = new Date();
		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
			.sign(Algorithm.HMAC512(secretKey));
	}

	public String extractEmailFromAccessToken(String accessToken) {
		return Optional.of(
				JWT.require(Algorithm.HMAC512(secretKey))
					.build()
					.verify(accessToken)
					.getClaim(EMAIL_CLAIM).asString())
			.orElseThrow(NotFoundEmailException::new);
	}

	public boolean isTokenValid(String token) {
		try {
			JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
			return true;
		} catch (JWTVerificationException e) {
			return false;
		}
	}
}
