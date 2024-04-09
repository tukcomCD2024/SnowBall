package com.example.memetory.domain.member.controller;

import static com.example.memetory.domain.member.MemberFixture.*;
import static org.mockito.BDDMockito.*;
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
import com.example.memetory.domain.member.dto.MemberUpdateDto;
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
	private RefreshTokenService refreshTokenService;
	@MockBean
	private MemberService memberService;
	@MockBean
	private JwtService jwtService;
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
			.withClaim("email", MEMBER.getEmail())
			.sign(Algorithm.HMAC512(secretKey));
	}

	protected String toRequestBody(Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}

	@Test
	@DisplayName("멤버 업데이트 성공")
	public void 멤버_업데이트_성공() throws Exception {
		// given -> 결과에 대한 객체, Member 객체 저장할 필요 존재
		given(memberRepository.findByEmail(MEMBER.getEmail())).willReturn(Optional.ofNullable(MEMBER));

		// when
		final ResultActions perform = mockMvc.perform(
			post("/member")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toRequestBody(new MemberUpdateDto("junrain2", "imageUrl2")))
				.header("Authorization", "Bearer " + authorization_jwt)
		);

		// then
		perform.andExpect(status().isOk());
	}

	@Test
	@DisplayName("멤버 업데이트 성공(닉네임 중복)")
	public void 멤버_업데이트_실패_닉네임_중복() throws Exception {
		// given -> 결과에 대한 객체, Member 객체 저장할 필요 존재
		given(memberService.isDuplicatedNickname(any())).willReturn(true);

		// when
		final ResultActions perform = mockMvc.perform(
			post("/member")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toRequestBody(new MemberUpdateDto("junrain", "imageUrl")))
				.header("Authorization", "Bearer " + authorization_jwt)
		);

		// then
		perform.andExpect(status().isConflict());
	}
}
