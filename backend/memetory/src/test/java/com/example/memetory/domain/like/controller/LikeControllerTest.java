package com.example.memetory.domain.like.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.exception.NotFoundLikeException;
import com.example.memetory.domain.like.service.LikeService;
import com.example.memetory.domain.memes.exception.NotFoundMemesException;
import com.example.memetory.global.LoginTest;

@DisplayName("Like 컨트롤러 테스트의 ")
@WebMvcTest(LikeController.class)
public class LikeControllerTest extends LoginTest {
	@MockBean
	private LikeService likeService;

	@DisplayName("좋아요 등록 성공")
	@Test
	void 좋아요_등록() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(
			post("/memes/-1/like")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		verify(likeService).register(any(LikeServiceDto.class));
		perform.andExpect(status().isCreated());
	}

	@DisplayName("좋아요 취소 성공")
	@Test
	void 좋아요_취소() throws Exception {
		// when
		final ResultActions perform = mockMvc.perform(
			delete("/memes/-1/like")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		verify(likeService).cancel(any(LikeServiceDto.class));
		perform.andExpect(status().isOk());
	}

	@DisplayName("좋아요 취소 실패")
	@Test
	void 좋아요_취소_실패() throws Exception {
		// when
		doThrow(new NotFoundLikeException()).when(likeService).cancel(any(LikeServiceDto.class));

		final ResultActions perform = mockMvc.perform(
			delete("/memes/-1/like")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isNotFound());
	}
}
