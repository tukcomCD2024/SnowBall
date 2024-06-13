package com.example.memetory.domain.memes;

import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static com.example.memetory.global.response.ResultCode.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.repository.MemesRepository;
import com.example.memetory.global.integration.BaseIntegrationTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MemesLikeIntegrationTest extends BaseIntegrationTest {
	@Autowired
	MemeRepository memeRepository;
	@Autowired
	MemesRepository memesRepository;

	@Test
	@DisplayName("memesId를 통한 좋아요 등록 성공")
	void Given_memesId_When_registerLike_Then_CREATE_LIKE_SUCCESS() {
		Meme meme = memeRepository.save(MEME(member));
		Memes memes = memesRepository.save(MEMES(member, meme));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.post("/memes/{memesId}/like", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(MESSAGE);

		// then
		assertThat(result).isEqualTo(CREATE_LIKE_SUCCESS.getMessage());
		memesRepository.findByMemesId(memes.getId())
			.ifPresent(m -> assertThat(m.getLikeCount()).isEqualTo(2L));
	}
}
