package com.example.memetory.domain.meme.controller;

import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.global.response.ErrorCode.*;
import static com.example.memetory.global.response.ResultCode.*;
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
	@DisplayName("이메일과 밈id를 통한 멤버의 MemeResponse 반환 성공")
	void Given_emailAndMemeId_When_findMemberMemeResponse_Then_Member_MemeResponse() throws Exception {
		// given
		MemeResponse returnedMemeResponse = MemeResponse.of(MEME(loginMember));
		given(memeService.findMemberMemeResponse(any())).willReturn(returnedMemeResponse);

		// when
		final ResultActions perform = mockMvc.perform(
				get("/meme/-1")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE, GET_ONE_MEME_SUCCESS.getMessage()).exists())
			.andExpect(jsonPath("$.data.s3Url").exists());
	}

	@Test
	@DisplayName("권한이 없는 멤버 이메일로 인한 AccessDeniedMemeException 반환")
	void Given_NotPermissionMemberId_When_findMemberMemeResponse_Throw_AccessDeniedMemberMemeException() throws Exception {
		// given
		given(memeService.findMemberMemeResponse(any(MemeServiceDto.class))).willThrow(new AccessDeniedMemeException());

		// when
		final ResultActions perform = mockMvc.perform(
				get("/meme/-1")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isForbidden())
			.andExpect(jsonPath(ERROR_MESSAGE, MEME_ACCESS_DENY.getMessage()).exists());
	}

	@Test
	@DisplayName("존재하지 않는 memeId로 인한 NotFoundMemeException 반환")
	void Given_NotExistMemeId_Then_findMemberMemeResponse_Throw_NotFoundMemberMemeException() throws Exception {
		// given
		Long notExistMemeId = -1L;

		given(memeService.findMemberMemeResponse(any(MemeServiceDto.class))).willThrow(new NotFoundMemeException());

		// when
		final ResultActions perform = mockMvc.perform(
				get("/meme/" + notExistMemeId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isNotFound())
			.andExpect(jsonPath(ERROR_MESSAGE, MEME_NOT_FOUND.getMessage()).exists());
	}

	@Test
	@DisplayName("이메일과 Pageable을 통한 멤버의 MemePageResponse 반환 성공")
	void Given_emailAndPageable_When_findMemberMemePageResponse_Then_Member_MemePageResponse() throws Exception {
		// given
		List<MemeResponse> memeList = List.of(MEME(loginMember), OTHER_MEME(loginMember))
			.stream()
			.map(MemeResponse::of)
			.toList();
		MemePageResponse memePageResponse = MemePageResponse.builder().memeList(memeList).build();

		given(memeService.findMemberMemePageResponse(any(), any())).willReturn(memePageResponse);

		// when
		final ResultActions perform = mockMvc.perform(
			get("/meme?page=0&size=10").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)).andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE).value(GET_MEMBER_MEME_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.data.memeList").isArray());
	}
}
