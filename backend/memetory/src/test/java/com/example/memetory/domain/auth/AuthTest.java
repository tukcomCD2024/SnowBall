package com.example.memetory.domain.auth;

import static com.example.memetory.domain.member.MemberFixture.*;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.memetory.domain.member.controller.MemberController;
import com.example.memetory.domain.member.dto.MemberUpdateDto;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.LoginTest;
import com.example.memetory.global.security.jwt.dto.TokenResponse;
import com.example.memetory.global.security.jwt.refresh.domain.RefreshToken;

@DisplayName("JWT 인증테스트의 ")
@WebMvcTest(MemberController.class)
public class AuthTest extends LoginTest {

	@MockBean
	private MemberService memberService;

	@Test
	@DisplayName("Access Token을 이용한 정상 인가(Bearer)")
	public void accessToken_인가_성공_with_Bearer() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(new MemberUpdateDto("junrain2", "imageUrl2")))
			.header("Authorization", "Bearer " + accessToken));

		// then
		perform.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Access Token을 이용한 정상 인가")
	public void accessToken_인가_성공() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(new MemberUpdateDto("junrain2", "imageUrl2")))
			.header("Authorization", accessToken));

		// then
		perform.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Access Token 기간 만료로 인한 멤버 업데이트 실패")
	public void access_token_기간만료() throws Exception {
		// given -> 시간이 만료된 accessToken 생성
		Date now = new Date();
		String accessToken = JWT.create()
			.withSubject("AccessToken")
			.withExpiresAt(new Date(now.getTime() - 1000))
			.withClaim("email", MEMBER().getEmail())
			.sign(Algorithm.HMAC512(secretKey));

		// when
		final ResultActions perform = mockMvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(new MemberUpdateDto("junrain2", "imageUrl2")))
			.header("Authorization", accessToken));

		// then
		perform.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Refresh Token 전송으로 인한 access_token refresh_token 재발급")
	public void refreshToken과_accessToken_재발급() throws Exception {
		// given -> refresh Token 세팅 및 redis 에서 refresh Token이 있는지 조회
		Date now = new Date();
		String refreshToken = JWT.create()
			.withSubject("RefreshToken")
			.withExpiresAt(new Date(now.getTime() + 18000))
			.sign(Algorithm.HMAC512(secretKey));

		TokenResponse expect = TokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();

		RefreshToken token = new RefreshToken(MEMBER().getEmail());
		given(refreshTokenService.findByToken(refreshToken)).willReturn(token);

		// when
		final ResultActions perform = mockMvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(new MemberUpdateDto("junrain2", "imageUrl2")))
			.header("Authorization-refresh", refreshToken)).andDo(print());

		// then
		perform.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("accessToken", is(accessToken)).exists())
			.andExpect(jsonPath("refreshToken", is(refreshToken)).exists());
	}

	@Test
	@DisplayName("Refresh Token 만료")
	public void refreshToken_만료() throws Exception {
		// given -> 만료된 refreshToken 설정
		Date now = new Date();
		String refreshToken = JWT.create()
			.withSubject("RefreshToken")
			.withExpiresAt(new Date(now.getTime() - 1000))
			.sign(Algorithm.HMAC512(secretKey));

		// when
		final ResultActions perform = mockMvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
			.content(toRequestBody(new MemberUpdateDto("junrain2", "imageUrl2")))
			.header("Authorization-refresh", refreshToken)).andDo(print());

		// then
		perform.andExpect(status().isUnauthorized());
	}
}
