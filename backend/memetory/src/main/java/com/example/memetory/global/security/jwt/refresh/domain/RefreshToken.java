package com.example.memetory.global.security.jwt.refresh.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash(value = "refreshToken")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RefreshToken {

	@Id
	private String email;

	@Indexed
	private String refreshToken;

	@TimeToLive()
	private Long expirationPeriod;

	public RefreshToken(String email) {
		this.email = email;
	}

	public void setRefreshToken(String refreshToken, Long expiration) {
		this.refreshToken = refreshToken;
		this.expirationPeriod = expiration;
	}
}
