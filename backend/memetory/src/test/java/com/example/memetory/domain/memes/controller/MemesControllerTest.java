package com.example.memetory.domain.memes.controller;

import static com.example.memetory.domain.meme.MemeFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.service.MemesService;
import com.example.memetory.global.LoginTest;

@DisplayName("Memes 컨트롤러 테스트의 ")
@WebMvcTest(MemesController.class)
public class MemesControllerTest extends LoginTest {
	@MockBean
	private MemesService memesService;

	@Test
	@DisplayName("Memes 생성")
	public void memes_생성() throws Exception {
		// given
		Meme meme = FIRST_MEME(loginMember);

		// when
		final ResultActions perform = mockMvc.perform(
			post("/memes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toRequestBody(new GenerateMemesRequest(meme.getId(), "new Title")))
				.header("Authorization", "Bearer " + accessToken)
		).andDo(print());

		// then
		perform.andExpect(status().isCreated());

	}
}
