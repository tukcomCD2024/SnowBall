package com.example.memetory.domain.member;

import static com.example.memetory.domain.member.MemberFixture.*;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.example.memetory.domain.member.dto.request.MemberUpdateRequest;
import com.example.memetory.domain.member.dto.response.MemberResponse;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.integration.BaseIntegrationTest;
import com.example.memetory.global.security.jwt.util.JwtUtil;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MemberIntegrationTest extends BaseIntegrationTest {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private JwtUtil jwtUtil;

	private Member member;
	private String accessToken;

	@BeforeEach
	void setUpAccessToken() {
		member = memberRepository.save(MEMBER());
		accessToken = jwtUtil.generateAccessToken(member.getEmail());
	}

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
}