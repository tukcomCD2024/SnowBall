package com.example.memetory.domain.memes;

import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.global.response.ErrorCode.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.repository.MemesRepository;
import com.example.memetory.global.integration.BaseIntegrationTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("Memes 통합 테스트의 ")
public class MemesIntegrationTest extends BaseIntegrationTest {
	@Autowired
	private MemeRepository memeRepository;
	@Autowired
	private MemesRepository memesRepository;

	@Test
	@DisplayName("GenerateMemesRequest을 통한 Memes 생성 성공")
	public void Given_GenerateMemesRequest_When_registerMemes_Then_MemesResponse() {
		// given
		Meme meme = memeRepository.save(MEME(member));

		final String title = "new Memes";
		GenerateMemesRequest request = new GenerateMemesRequest(meme.getId(), title);

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(request)
				.when()
				.post("/memes")
				.then()
				.log()
				.all()
				.extract();

		MemesResponse result = response.jsonPath().getObject("data", MemesResponse.class);

		// then
		assertThat(result.getTitle()).isEqualTo(title);
	}

	@Test
	@DisplayName("존재하지 않는 MemeId로 인한 MEME_NOT_FOUND 반환")
	public void Given_NotExistedMemeId_When_findMemberMemePageResponse_Throw_NotFoundMemberMemeException() {
		// given
		final String title = "new Memes";
		GenerateMemesRequest request = new GenerateMemesRequest(-1L, title);

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(request)
				.when()
				.post("/memes")
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(ERROR_MESSAGE);

		// then
		assertThat(result).isEqualTo(MEME_NOT_FOUND.getMessage());
	}
}
