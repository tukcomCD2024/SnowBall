package com.example.memetory.global.security.jwt.refresh.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.memetory.global.security.jwt.refresh.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
