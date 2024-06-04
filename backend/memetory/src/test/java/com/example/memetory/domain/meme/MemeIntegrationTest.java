package com.example.memetory.domain.meme;

import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.global.response.ErrorCode.*;
import static com.example.memetory.global.response.ResultCode.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.example.memetory.domain.meme.dto.GenerateMemeListRequest;
import com.example.memetory.domain.meme.dto.MemePageResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.global.integration.BaseIntegrationTest;
import com.example.memetory.global.integration.MockServer;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("Meme 통합 테스트의 ")
public class MemeIntegrationTest extends BaseIntegrationTest {
	@Autowired
	private MemeRepository memeRepository;

	@Test
	@DisplayName("GenerateMemeListRequest를 통한 CREATE_MEME_SUCCESS Message 반환")
	void Given_GenerateMemeListRequest_When_registerMeme_Then_Message_CREATE_MEME_SUCCESS() {
		// given
		GenerateMemeListRequest request = GENERATE_MEME_LIST_REQUEST();

		MockServer.startMemeServerFromGenerateMemeListRequest();

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(request)
				.when()
				.post("/meme")
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(MESSAGE);

		// then
		assertThat(result).isEqualTo(CREATE_MEME_SUCCESS.getMessage());
	}

	@Test
	@DisplayName("밈id를 통한 멤버의 MemeResponse 반환 성공")
	void Given_MemeId_When_findMemberMemeResponse_Then_Member_MemeResponse() {
		// given
		Meme savedMeme = memeRepository.save(MEME(member));
		MemeResponse expectedResult = MemeResponse.of(savedMeme);

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.get("/meme/" + savedMeme.getId())
				.then()
				.log()
				.all()
				.extract();

		MemeResponse result = response.jsonPath().getObject("data", MemeResponse.class);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("AccessToken을 통한 멤버의 MemePageResponse 반환")
	void Given_AccessToken_When_findMemberMemeResponse_Then_Member_MemePageResponse() {
		// given
		List<Meme> memeList = memeRepository.saveAll(
			List.of(MEME(member), OTHER_MEME(member), MEME(member), OTHER_MEME(member)));

		List<MemeResponse> expectedResult = memeList.stream().map(MemeResponse::of).toList();

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.get("/meme?page=0&size=10")
				.then()
				.log()
				.all()
				.extract();

		MemePageResponse result = response.jsonPath().getObject("data", MemePageResponse.class);

		// then
		assertThat(result.getMemeList()).usingRecursiveComparison().isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("존재하지 않는 MemeId로 인한 MEME_NOT_FOUND 반환")
	void Given_MemeId_When_findMemberMemeResponse_Throw_NotFoundMemberMemeException() {
		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.get("/meme/1")
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(ERROR_MESSAGE);

		// then
		assertThat(result).isEqualTo(MEME_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 MemeId로 인한 MEME_NOT_FOUND 반환")
	void Given_MemeId_When_findMemberMemePageResponse_Throw_NotFoundMemberMemeException() {
		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.get("/meme/1")
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(ERROR_MESSAGE);

		// then
		assertThat(result).isEqualTo(MEME_NOT_FOUND.getMessage());
	}

}
