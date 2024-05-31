package com.example.memetory.domain.auth;

import static com.example.memetory.domain.auth.AuthFixture.*;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import com.example.memetory.domain.auth.dto.LoginRequest;
import com.example.memetory.domain.member.repository.MemberRepository;
import com.example.memetory.global.integration.BaseIntegrationTest;
import com.example.memetory.global.security.jwt.dto.TokenResponse;
import com.example.memetory.global.security.jwt.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@ActiveProfiles("test")
@DisplayName("Auth 통합 테스트의 ")
@WireMockTest(httpPort = 9899)
public class AuthIntegrationTest extends BaseIntegrationTest {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private JwtService jwtService;

	@DisplayName("LoginRequest 통한 멤버 생성 성공")
	@Test
	void Given_LoginRequest_When_login_Then_Return_HttpStatus_OK() throws JsonProcessingException {

		LoginRequest loginRequest = GOOGLE_LOGIN_REQUEST();

		stubFor(get("/google-url")
			.withHeader("Authorization", equalTo("Bearer " + loginRequest.getToken()))
			.willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody(JSON_GOOGLE_OAUTH2_RESPONSE())
			));

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(JSON_GOOGLE_LOGIN_REQUEST())
				.when()
				.post("/login")
				.then()
				.log()
				.all()
				.extract();

		TokenResponse result = response.as(TokenResponse.class, ObjectMapperType.JACKSON_2);
		// then
		assertTrue(jwtService.isTokenValid(result.getAccessToken()));
		assertTrue(jwtService.isTokenValid(result.getRefreshToken()));
	}
}
