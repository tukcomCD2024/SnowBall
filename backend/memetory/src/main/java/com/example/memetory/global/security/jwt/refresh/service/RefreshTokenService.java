package com.example.memetory.global.security.jwt.refresh.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;
import com.example.memetory.global.security.jwt.refresh.domain.RefreshToken;
import com.example.memetory.global.security.jwt.refresh.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.refresh.expiration}")
	private Long expirationPeriod;

	public void updateToken(String email, String token) {
		RefreshToken refreshToken = refreshTokenRepository.findById(email).orElse(new RefreshToken(email));
		refreshToken.setRefreshToken(token, expirationPeriod / 1000);
		log.info("토큰 값 : " + refreshToken.getRefreshToken());
		log.info("이메일 : " + refreshToken.getEmail());
		log.info("유효기간 : " + refreshToken.getExpirationPeriod());
		refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken findByToken(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
			.orElseThrow(NotFoundTokenException::new);

		return refreshToken;
	}
}
