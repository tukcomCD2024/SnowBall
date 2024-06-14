package com.example.memetory.domain.memes;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.example.memetory.domain.memes.MemesFixture.*;
import static com.example.memetory.global.response.ErrorCode.*;
import static com.example.memetory.global.response.ResultCode.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.meme.entity.Meme;
import com.example.memetory.domain.meme.repository.MemeRepository;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesInfoSliceResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.entity.Memes;
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

	@Test
	@DisplayName("memesId를 통한 밈스 삭제 성공")
	public void Given_memesId_When_deleteMemes_Then_DELETE_MEMES_SUCCESS() {
		// given
		Meme meme = memeRepository.save(MEME(member));
		Memes memes = memesRepository.save(MEMES(member, meme));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.delete("/memes/{memesId}", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(MESSAGE);

		// then
		assertThat(result).isEqualTo(DELETE_MEMES_SUCCESS.getMessage());
	}

	@DisplayName("권한 없는 멤버로 인한 MEMBER_ACCESS_DENY 반환")
	@Test
	public void Given_unAuthorizedMember_When_deleteMemes_Then_MEMES_ACCESS_DENY() {
		// given
		Member unAuthorizationMember = memberRepository.save(OTHER_MEMBER());
		Meme meme = memeRepository.save(MEME(unAuthorizationMember));
		Memes memes = memesRepository.save(MEMES(unAuthorizationMember, meme));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.delete("/memes/{memesId}", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().get(ERROR_MESSAGE);

		// then
		assertThat(result).isEqualTo(MEMBER_ACCESS_DENY.getMessage());
	}

	@Test
	@DisplayName("memesId를 통한 MemesResponse 반환 성공")
	public void Given_memesId_When_findMemesResponse_Thee_MemeResponse() {
		Meme meme = memeRepository.save(MEME(member));
		Memes memes = memesRepository.save(MEMES(member, meme));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.get("/memes/{memesId}", memes.getId())
				.then()
				.log()
				.all()
				.extract();

		MemesResponse result = response.jsonPath().getObject("data", MemesResponse.class);

		// then
		assertThat(result.getMemesId()).isEqualTo(memes.getId());
	}

	@Test
	@DisplayName("Pageable을 통한 MemesInfoSliceResponse 반환 성공")
	public void Given_Pageable_When_findMemesInfoSliceResponse_Then_MemesInfoPageResponse() throws Exception {
		final int SIZE = 10;
		Meme meme = memeRepository.save(MEME(member));

		for (int i = 0; i < SIZE + 1; i++) {
			memesRepository.save(MEMES(member, meme));
		}

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.when()
				.get("/memes?page={page}&size={size}", 0, SIZE)
				.then()
				.log()
				.all()
				.extract();

		MemesInfoSliceResponse result = response.jsonPath().getObject("data", MemesInfoSliceResponse.class);

		// then
		assertTrue(result.isHasNext());
		assertThat(result.getMemesInfoResponseList()).hasSize(SIZE);
	}
}
