package com.example.memetory.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
import com.example.memetory.global.security.jwt.service.JwtService;

import lombok.RequiredArgsConstructor;

@TestConfiguration
@RequiredArgsConstructor
public class SecurityTestConfig {
	private final JwtService jwtService;
	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.formLogin(AbstractHttpConfigurer::disable)  // 기본 제공하는 로그인 Form 사용 X
			.httpBasic(AbstractHttpConfigurer::disable)  // Bearer방식이기 때문에 httpBasic 사용 X
			.csrf(AbstractHttpConfigurer::disable)  //csrf 보안 사용 X
			.sessionManagement((sessionManagement) ->   // 세션은 사용하지 않기 때문에 stateless 설정
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorizeRequests   // 개발을 진행하기 위해 일단 모든 url 허용
				-> authorizeRequests.anyRequest().permitAll());

		http.addFilterAfter(jwtAuthenticationProcessingFilter(), LogoutFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
		return new JwtAuthenticationProcessingFilter(jwtService, memberRepository, refreshTokenService);
	}
}
