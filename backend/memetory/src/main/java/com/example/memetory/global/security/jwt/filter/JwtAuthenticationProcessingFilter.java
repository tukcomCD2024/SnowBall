package com.example.memetory.global.security.jwt.filter;

import static jakarta.servlet.http.HttpServletResponse.*;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.exception.NotFoundTokenException;
import com.example.memetory.global.security.jwt.refresh.domain.RefreshToken;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
import com.example.memetory.global.security.jwt.service.JwtService;
import com.example.memetory.global.util.PasswordUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

	private static final String NO_CHECK_URL = "/login";

	private final JwtService jwtService;
	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		if (request.getRequestURI().equals(NO_CHECK_URL)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String refreshToken = jwtService.extractRefreshToken(request);
			checkRefreshTokenAndReIssueAccessToken(response, refreshToken);

			response.setStatus(SC_UNAUTHORIZED);
		} catch (NotFoundTokenException e) {
			checkAccessTokenAndAuthentication(request, response, filterChain);
		}
	}

	private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
		if (jwtService.isTokenValid(refreshToken)) {
			RefreshToken refresh = refreshTokenService.findByToken(refreshToken);
			jwtService.sendAccessAndRefreshToken(response, refresh.getEmail());
		}
	}

	private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String email = jwtService.getEmail(request);
			memberRepository.findByEmail(email).ifPresent(this::saveAuthentication);
		} catch (Exception e) {
			response.setStatus(SC_FORBIDDEN);
		} finally {
			filterChain.doFilter(request, response);
		}
	}

	private void saveAuthentication(Member myMember) {
		String password = PasswordUtil.generateRandomPassword();

		UserDetails userDetailsUser = User.builder()
			.username(myMember.getEmail())
			.password(password)
			.roles(myMember.getRole().name())
			.build();

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null,
			authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
