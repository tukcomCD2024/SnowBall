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
import com.example.memetory.domain.member.dto.request.MemberUpdateRequest;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.BaseControllerTest;
import com.example.memetory.global.security.jwt.refresh.domain.RefreshToken;

@DisplayName("JWT 인증테스트의 ")
@WebMvcTest(MemberController.class)
public class AuthControllerTest extends BaseControllerTest {
	@MockBean
	private MemberService memberService;

	@Test
	@DisplayName("Bearer+AccessToken을 통한 정상 인증")
	public void Given_AccessTokenWithBearer_When_JwtFilter_Expect_Authorization() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(
				post("/member")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toRequestBody(new MemberUpdateRequest("junrain2", "imageUrl2")))
					.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isOk());
	}

	@Test
	@DisplayName("AccessToken을 통한 정상 인증")
	public void Given_AccessToken_When_JwtFilter_Expect_Authorization() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(
				post("/member")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toRequestBody(new MemberUpdateRequest("junrain2", "imageUrl2")))
					.header("Authorization", accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isOk());
	}

	@Test
	@DisplayName("만료된 AccessToken 인한 인증 실패")
	public void Given_ExpiredAccessToken_When_JwtFilter_Expect_IsForbidden() throws Exception {
		// given
		Date now = new Date();
		String expiredAccessToken = JWT.create()
			.withSubject("AccessToken")
			.withExpiresAt(new Date(now.getTime() - 1000))
			.withClaim("email", MEMBER().getEmail())
			.sign(Algorithm.HMAC512(secretKey));

		// when
		final ResultActions perform = mockMvc.perform(
				post("/member").contentType(MediaType.APPLICATION_JSON)
					.content(toRequestBody(new MemberUpdateRequest("junrain2", "imageUrl2")))
					.header("Authorization", expiredAccessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("RefreshToken 전송으로 인한 AccessToken과 RefreshToken 재발급")
	public void Given_RefreshToken_When_JwtFilter_Expect_ReissueAccessTokenAndRefreshToken() throws Exception {
		// given
		Date now = new Date();
		String refreshToken = JWT.create()
			.withSubject("RefreshToken")
			.withExpiresAt(new Date(now.getTime() + 18000))
			.sign(Algorithm.HMAC512(secretKey));

		RefreshToken token = new RefreshToken(MEMBER().getEmail());
		given(refreshTokenService.findByToken(refreshToken)).willReturn(token);

		// when
		final ResultActions perform = mockMvc.perform(
				post("/member").contentType(MediaType.APPLICATION_JSON)
					.content(toRequestBody(new MemberUpdateRequest("junrain2", "imageUrl2")))
					.header("Authorization-refresh", refreshToken)).andDo(print())
			.andDo(print());

		// then
		perform.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("accessToken", is(accessToken)).exists())
			.andExpect(jsonPath("refreshToken", is(refreshToken)).exists());
	}

	@Test
	@DisplayName("만료된 RefreshToken으로 인한 인증 실패")
	public void Given_ExpiredRefreshToken_When_JwtFilter_Expect_IsUnauthorized() throws Exception {
		// given
		Date now = new Date();
		String expiredRefreshToken = JWT.create()
			.withSubject("RefreshToken")
			.withExpiresAt(new Date(now.getTime() - 1000))
			.sign(Algorithm.HMAC512(secretKey));

		// when
		final ResultActions perform = mockMvc.perform(
			post("/member").contentType(MediaType.APPLICATION_JSON)
				.content(toRequestBody(new MemberUpdateRequest("junrain2", "imageUrl2")))
				.header("Authorization-refresh", expiredRefreshToken))
			.andDo(print());

		// then
		perform.andExpect(status().isUnauthorized());
	}
}
