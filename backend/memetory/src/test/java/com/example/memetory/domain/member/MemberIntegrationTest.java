package com.example.memetory.domain.member;

import static com.example.memetory.domain.member.MemberFixture.*;
import static com.example.memetory.global.response.ErrorCode.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.example.memetory.domain.member.dto.request.MemberUpdateRequest;
import com.example.memetory.domain.member.dto.response.MemberResponse;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.global.integration.BaseIntegrationTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MemberIntegrationTest extends BaseIntegrationTest {

	@Test
	@DisplayName("MemberUpdateRequest을 통한 MemberResponse 반환 성공")
	void Given_MemberUpdateRequest_When_updateMember_Then_MemberUpdateResponse() {
		// given
		MemberUpdateRequest request = MEMBER_UPDATE_REQUEST();

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(request)
				.when()
				.post("/member")
				.then()
				.log()
				.all()
				.extract();

		MemberResponse result = response.jsonPath().getObject("data", MemberResponse.class);
		Member updateMember = memberRepository.findById(member.getId()).get();

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(MemberResponse.of(updateMember));
	}

	@Test
	@DisplayName("MemberUpdateRequest을 통한 errorMessage 닉네임 중복 반환")
	void Given_DUPLICATED_NICKNAME_When_updateMember_Then_NICKNAME_IS_DUPLICATED() {
		// given
		MemberUpdateRequest request = DUPLICATED_NICKNAME_MEMBER_UPDATE_REQUEST();

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(request)
				.when()
				.post("/member")
				.then()
				.log()
				.all()
				.extract();

		String result = response.jsonPath().getObject(ERROR_MESSAGE, String.class);

		// then
		assertThat(result).isEqualTo(NICKNAME_IS_DUPLICATED.getMessage());
	}

	@Test
	@DisplayName("엑세스 토큰을 통한 회원의 멤버 조회 성공")
	void Given_accessToken_When_findMember_Then_MemberResponse() {
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.get("/member")
				.then()
				.log()
				.all()
				.extract();

		MemberResponse result = response.jsonPath().getObject("data", MemberResponse.class);
		Member expectedMember = memberRepository.findById(member.getId()).get();

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(MemberResponse.of(expectedMember));
	}
}