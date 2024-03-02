package com.example.memetory.global.security.jwt.refresh;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash(value = "refreshToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

	@Id
	private String email;

	private String refreshToken;

	@TimeToLive
	private Long expirationPeriod;

	public void setRefreshToken(String refreshToken, Long expiration) {
		this.refreshToken = refreshToken;
		this.expirationPeriod = expiration;
	}

	public RefreshToken(String email) {
		this.email = email;
	}
}
