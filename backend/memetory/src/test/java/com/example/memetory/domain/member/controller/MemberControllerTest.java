package com.example.memetory.domain.member.controller;

import static com.example.memetory.domain.member.MemberFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.memetory.config.SecurityTestConfig;
import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.security.jwt.refresh.service.RefreshTokenService;
import com.example.memetory.global.security.jwt.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@DisplayName("Member 컨트롤러 테스트의 ")
@WebMvcTest(MemberController.class)
@Import(SecurityTestConfig.class)
public class MemberControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private JwtService jwtService;
	@MockBean
	private MemberService memberService;
	@MockBean
	private RefreshTokenService refreshTokenService;
	@MockBean
	private MemberRepository memberRepository;

	@Value("${jwt.secretKey}")
	private String secretKey;
	private String authorization_jwt;

	@BeforeEach
	void setUp() {
		Date now = new Date();
		authorization_jwt = JWT.create()
			.withSubject("AccessToken")
			.withExpiresAt(new Date(now.getTime() + 180000))
			.withClaim("email", GUEST_MEMBER.getId())
			.sign(Algorithm.HMAC512(secretKey));
	}

	// 잘 된 테스트인지는 아직 의문
	@Test
	@DisplayName("회원가입이 완료되었는가")
	void 회원가입() throws Exception {
		// given -> 결과에 대한 객체, Member 객체 저장할 필요 존재
		given(memberRepository.findByEmail(GUEST_MEMBER.getEmail())).willReturn(Optional.ofNullable(GUEST_MEMBER));

		// when
		final ResultActions perform = mockMvc.perform(
			post("/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toRequestBody(new MemberSignUpRequest("Memetory")))
				.header("Authorization", "Bearer " + authorization_jwt)
		);

		// then
		perform.andExpect(status().isOk());
	}

	protected String toRequestBody(Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}
}
