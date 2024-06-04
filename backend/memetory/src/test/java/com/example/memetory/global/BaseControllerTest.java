package com.example.memetory.global;

import static com.example.memetory.domain.member.MemberFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.security.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
import com.example.memetory.global.security.jwt.service.JwtService;
import com.example.memetory.global.security.jwt.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public abstract class BaseControllerTest {
	protected final String MESSAGE = "$.message";
	protected final String ERROR_MESSAGE = "$.errorMessage";

	protected MockMvc mockMvc;
	protected String accessToken;
	protected Member loginMember;

	@SpyBean
	protected JwtService jwtService;
	@MockBean
	protected RefreshTokenService refreshTokenService;
	@MockBean
	protected MemberRepository memberRepository;
	@SpyBean
	protected JwtUtil jwtUtil;
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

		accessToken = jwtUtil.generateAccessToken(loginMember.getEmail());
	}
}
