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
import com.example.memetory.domain.like.exception.NotCreateLikeException;
import com.example.memetory.domain.like.exception.NotFoundLikeException;
import com.example.memetory.domain.like.service.LikeService;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesInfoResponse;
import com.example.memetory.domain.memes.dto.response.MemesInfoSliceResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.exception.AccessDinedMemesException;
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
	@DisplayName("GenerateMemesRequest을 통한 Memes 생성 성공")
	public void Given_GenerateMemesRequest_When_registerMemes_Then_MemesResponse() throws Exception {
		// given
		final String title = "new Memes";

		Meme meme = MEME(loginMember);

		GenerateMemesRequest request = new GenerateMemesRequest(meme.getId(), title);
		MemesResponse response = MemesResponse.of(MEMES(loginMember, meme));

		given(memesService.registerMemes(any(MemesServiceDto.class))).willReturn(response);

		// when
		final ResultActions perform = mockMvc.perform(
			post("/memes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toRequestBody(request))
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isCreated())
			.andExpect(jsonPath(MESSAGE).value(CREATE_MEMES_SUCCESS.getMessage()))
			.andExpect(jsonPath("$.data.title").value(title));
	}

	@Test
	@DisplayName("memesId를 통한 밈스 삭제 성공")
	public void Given_memesId_When_deleteMemes_Then_DELETE_MEMES_SUCCESS() throws Exception {
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
	@DisplayName("권한 없는 멤버로 인한 MEMES_ACCESS_DENY 반환")
	public void Given_unAuthorizedMember_When_deleteMemes_Then_MEMES_ACCESS_DENY() throws Exception {
		// given
		doThrow(new AccessDinedMemesException()).when(memesService).deleteMemes(any());

		// when
		final ResultActions perform = mockMvc.perform(
			delete("/memes/-1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isForbidden())
			.andExpect(jsonPath(ERROR_MESSAGE).value(MEMES_ACCESS_DENY.getMessage()));
	}

	@Test
	@DisplayName("올타임 좋아요 Top 10 밈스 반환 성공")
	public void When_findTopMemesByLike_Then_GET_TOP_TEN_MEMES_SUCCESS() throws Exception {
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
	@DisplayName("주간 좋아요 Top 10 밈스 반환 성공")
	public void When_findTopMemesByLikeForWeek_Then_GET_WEEK_TOP_TEN_MEMES_SUCCESS() throws Exception {
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
	@DisplayName("밈스 id를 통한 MemesResponse 반환 성공")
	public void Given_memesId_When_findMemesResponse_Thee_MemeResponse() throws Exception {
		Meme meme = MEME(loginMember);
		MemesResponse response = MemesResponse.of(MEMES(loginMember, meme));
		given(memesService.findMemesResponse(any())).willReturn(response);

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
	@DisplayName("Pageable을 통한 MemesInfoSliceResponse 반환 성공")
	public void Given_Pageable_When_findMemesInfoSliceResponse_Then_MemesInfoPageResponse() throws Exception {
		final String page = "page=0&size=10";
		Meme meme = MEME(loginMember);

		MemesInfoSliceResponse response = MemesInfoSliceResponse.builder()
			.memesInfoResponseList(List.of(MemesInfoResponse.of(MEMES(loginMember, meme))))
			.currentPage(0)
			.hasNext(false)
			.build();
		given(memesService.findMemesInfoSliceResponse(any())).willReturn(response);

		String expectedTitle = response.getMemesInfoResponseList().get(0).getTitle();

		// when
		final ResultActions perform = mockMvc.perform(
			get("/memes?" + page)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isOk()).andExpectAll(
			jsonPath(MESSAGE).value(GET_ALL_MEMES_SUCCESS.getMessage()),
			jsonPath("$.data.memesInfoResponseList[0].title").value(expectedTitle));
	}

	@Test
	@DisplayName("memesId를 통한 좋아요 등록 성공")
	void Given_memesId_When_registerLike_Then_CREATE_LIKE_SUCCESS() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(post("/memes/-1/like")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		verify(likeService).registerLike(any(LikeServiceDto.class));
		perform.andExpect(status().isCreated())
			.andExpect(jsonPath(MESSAGE).value(CREATE_LIKE_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("DB에 존재하는 좋아요로 인한 LIKE_NOT_CREATE 반환")
	void Given_ExistLike_When_registerLike_Then_LIKE_NOT_CREATE() throws Exception {
		// given
		doThrow(new NotCreateLikeException()).when(likeService).registerLike(any(LikeServiceDto.class));

		// when
		final ResultActions perform = mockMvc.perform(post("/memes/-1/like")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		verify(likeService).registerLike(any(LikeServiceDto.class));
		perform.andExpect(status().isConflict())
			.andExpect(jsonPath(ERROR_MESSAGE).value(LIKE_NOT_CREATE.getMessage()));
	}

	@Test
	@DisplayName("memesId를 통한 좋아요 삭제 성공")
	void Given_memesId_When_cancelLike_Then_DELETE_LIKE_SUCCESS() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(delete("/memes/-1/like")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		verify(likeService).cancelLike(any(LikeServiceDto.class));
		perform.andExpect(status().isOk())
			.andExpect(jsonPath(MESSAGE).value(DELETE_LIKE_SUCCESS.getMessage()));
	}

	@Test
	@DisplayName("DB에 없는 좋아요로 인한 LIKE_NOT_FOUND 반환")
	void Given_NotExistLike_When_cancelLike_Then_LIKE_NOT_FOUND() throws Exception {
		// given
		doThrow(new NotFoundLikeException()).when(likeService).cancelLike(any(LikeServiceDto.class));

		// when
		final ResultActions perform = mockMvc.perform(delete("/memes/-1/like")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken))
			.andDo(print());

		// then
		perform.andExpect(status().isNotFound())
			.andExpect(jsonPath(ERROR_MESSAGE).value(LIKE_NOT_FOUND.getMessage()));
	}
}
