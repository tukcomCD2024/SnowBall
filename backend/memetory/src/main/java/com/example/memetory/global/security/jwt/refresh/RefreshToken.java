package com.example.memetory.global.security.jwt.refresh;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RedisHash(value = "refreshToken")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

	@Id
	private String refreshToken;

	private String email;

	@TimeToLive
	private Long ExpirationPeriod;
}
