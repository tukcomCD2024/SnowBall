package com.example.memetory.domain.memes.controller;

import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
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

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.exception.NotFoundLikeException;
import com.example.memetory.domain.like.service.LikeService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesInfo;
import com.example.memetory.domain.memes.dto.response.MemesInfoSliceResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.exception.NotDeleteMemesException;
import com.example.memetory.domain.memes.service.MemesService;
import com.example.memetory.global.LoginTest;

@DisplayName("Memes 컨트롤러 테스트의 ")
@WebMvcTest(MemesController.class)
public class MemesControllerTest extends LoginTest {
	@MockBean
	private MemesService memesService;
	@MockBean
	private LikeService likeService;

	@Test
	@DisplayName("Memes 생성")
	public void 밈스_생성() throws Exception {
		// given
		Meme meme = MEME(loginMember);

		// when
		final ResultActions perform = mockMvc.perform(
			post("/memes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toRequestBody(new GenerateMemesRequest(meme.getId(), "new Title")))
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isCreated())
			.andExpect(jsonPath(MESSAGE).value(CREATE_MEMES_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("밈스 삭제")
	public void 밈스_삭제() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(
			delete("/memes/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE).value(DELETE_MEMES_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("밈스 삭제 실패")
	public void 밈스_삭제_실패() throws Exception {
		// given
		doThrow(new NotDeleteMemesException()).when(memesService).delete(any());

		// when
		final ResultActions perform = mockMvc.perform(
			delete("/memes/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isForbidden())
			.andExpect(jsonPath(ERROR_MESSAGE).value(MEMES_NOT_DELETE.getMessage()));
	}

	@Test
	@DisplayName("좋아요 Top 10 밈스 조회")
	public void TOP10_조회() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(
			get("/memes/like/all")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE).value(GET_TOP_TEN_MEMES_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("주간 좋아요 Top 10 밈스 조회")
	public void 주간_TOP10_조회() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(
			get("/memes/like/week")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE).value(GET_WEEK_TOP_TEN_MEMES_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("밈스 단일 조회 성공")
	public void 밈스_단일_조회_성공() throws Exception {
		Meme meme = MEME(loginMember);
		MemesResponse result = MemesResponse.of(MEMES(loginMember, meme));
		given(memesService.getMemesResponse(any())).willReturn(result);

		// when
		final ResultActions perform = mockMvc.perform(
			get("/memes/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isOk())
			.andExpectAll(
				jsonPath(MESSAGE).value(GET_ONE_MEMES_SUCCESS.getMessage()),
				jsonPath("$.data.likeCount").value("1"));
	}

	@Test
	@DisplayName("전체 밈스 슬라이스 조회 성공")
	public void 전체_밈스_슬라이스_조회_성공() throws Exception {
		Meme meme = MEME(loginMember);

		MemesInfoSliceResponse response = MemesInfoSliceResponse.builder()
			.memesInfoList(List.of(MemesInfo.of(MEMES(loginMember, meme))))
			.currentPage(0)
			.hasNext(false)
			.build();

		given(memesService.getMemesInfoSliceResponse(any())).willReturn(response);

		// when
		final ResultActions perform = mockMvc.perform(
			get("/memes?page=0&size=10")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isOk()).andExpectAll(
			jsonPath(MESSAGE).value(GET_ALL_MEMES_SUCCESS.getMessage()),
			jsonPath("$.data.memesInfoList[0].title").value(response.getMemesInfoList().get(0).getTitle()));
	}

	@DisplayName("좋아요 등록 성공")
	@Test
	void 좋아요_등록() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(post("/memes/-1/like").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + accessToken)).andDo(print());

		// then
		verify(likeService).register(any(LikeServiceDto.class));
		perform.andExpect(status().isCreated())
			.andExpect(jsonPath(MESSAGE).value(CREATE_LIKE_SUCCESS.getMessage()));
	}

	@DisplayName("좋아요 취소 성공")
	@Test
	void 좋아요_취소() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(delete("/memes/-1/like").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + accessToken)).andDo(print());

		// then
		verify(likeService).cancel(any(LikeServiceDto.class));
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE).value(DELETE_LIKE_SUCCESS.getMessage()));
	}

	@DisplayName("좋아요 취소 실패")
	@Test
	void 좋아요_취소_실패() throws Exception {
		// when
		doThrow(new NotFoundLikeException()).when(likeService).cancel(any(LikeServiceDto.class));

		final ResultActions perform = mockMvc.perform(delete("/memes/-1/like").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + accessToken)).andDo(print());

		// then
		perform.andExpect(status().isNotFound())
			.andExpect(jsonPath(ERROR_MESSAGE).value(LIKE_NOT_FOUND.getMessage()));
	}
}
