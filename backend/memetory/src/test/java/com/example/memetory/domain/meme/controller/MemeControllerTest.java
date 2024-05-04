package com.example.memetory.domain.meme.controller;

import static com.example.memetory.domain.meme.MemeFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.example.memetory.domain.meme.dto.MemePageResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.exception.AccessDeniedMemeException;
import com.example.memetory.domain.meme.exception.NotFoundMemeException;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.global.LoginTest;

@DisplayName("Meme 컨트롤러 테스트의 ")
@WebMvcTest(MemeController.class)
public class MemeControllerTest extends LoginTest {
	@MockBean
	private MemeService memeService;

	/**
	 *Todo
	 * register와 callback은 외부 API를 활용하는 메서드이기 떄문에 테스트 법 공부가 필요
	 */

	@Test
	@DisplayName("단일 밈 조회 성공")
	void 단일_밈_조회_성공() throws Exception {
		// given 예상될 MemeResponse 구현
		Meme meme = MEME(loginMember);
		given(memeService.getMeme(any())).willReturn(MemeResponse.of(meme));

		// when
		final ResultActions perform = mockMvc.perform(
			get("/meme/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath("$.s3Url").exists());
	}

	@Test
	@DisplayName("단일 밈 조회 실패, 로그인한 유저의 밈이 아닐 경우")
	void 단일_밈_조회_실패_유저인증_실패() throws Exception {
		// given 예상될 MemeResponse 구현
		given(memeService.getMeme(any(MemeServiceDto.class))).willThrow(new AccessDeniedMemeException());

		// when
		final ResultActions perform = mockMvc.perform(
			get("/meme/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("단일 밈 조회 실패, 존재하지 않는 밈일 경우")
	void 단일_밈_조회_실패_존재하지_않는_밈() throws Exception {
		// given 예상될 MemeResponse 구현
		Meme meme = MEME(loginMember);
		given(memeService.getMeme(any())).willThrow(new NotFoundMemeException());

		// when
		final ResultActions perform = mockMvc.perform(
			get("/meme/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("전체 밈 조회 성공")
	void 전체_밈_조회_성공() throws Exception {
		// given 예상될 MemeResponse 구현
		List<MemeResponse> memeList = List.of(MEME(loginMember), SECOND_MEME(loginMember)).stream()
			.map(MemeResponse::of)
			.toList();
		MemePageResponse memePageResponse = MemePageResponse.builder().memeList(memeList).build();

		given(memeService.getAllMeme(any(), any())).willReturn(memePageResponse);

		// when
		final ResultActions perform = mockMvc.perform(
			get("/meme?page=0&size=10")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath("$.memeList").isArray());
	}
}
