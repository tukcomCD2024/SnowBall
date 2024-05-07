package com.example.memetory.global;

import static com.example.memetory.domain.member.MemberFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
import com.example.memetory.global.security.jwt.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public abstract class LoginTest {
	protected final String MESSAGE = "message";
	protected final String ERROR_MESSAGE = "errorMessage";

	@Value("${jwt.secretKey}")
	protected String secretKey;
	protected MockMvc mockMvc;
	protected String accessToken;
	protected Member loginMember;

	@SpyBean
	protected JwtService jwtService;
	@MockBean
	protected RefreshTokenService refreshTokenService;
	@MockBean
	protected MemberRepository memberRepository;
	@Autowired
	private ObjectMapper objectMapper;

	protected String toRequestBody(Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}

	@BeforeEach
	public void loginSetup(WebApplicationContext ctx) {
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilter(new JwtAuthenticationProcessingFilter(jwtService, memberRepository, refreshTokenService))
			.alwaysDo(print())
			.build();

		loginMember = MEMBER();

		Date now = new Date();
		accessToken = JWT.create()
			.withSubject("AccessToken")
			.withExpiresAt(new Date(now.getTime() + 18000))
			.withClaim("email", loginMember.getEmail())
			.sign(Algorithm.HMAC512(secretKey));
	}
}
