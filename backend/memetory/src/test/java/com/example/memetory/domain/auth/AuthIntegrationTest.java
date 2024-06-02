package com.example.memetory.domain.auth;

import static com.example.memetory.domain.auth.AuthFixture.*;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.example.memetory.domain.auth.dto.LoginRequest;
import com.example.memetory.global.integration.BaseIntegrationTest;
import com.example.memetory.global.integration.MockServer;
import com.example.memetory.global.security.jwt.dto.TokenResponse;
import com.example.memetory.global.security.jwt.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("Auth 통합 테스트의 ")
@WireMockTest(httpPort = 9899)
public class AuthIntegrationTest extends BaseIntegrationTest {
	@Autowired
	private JwtService jwtService;

	@DisplayName("멤버가 DB에 없을 때, LoginRequest 통한 멤버 생성 성공")
	@Test
	void Given_LoginRequest_When_login_Then_CreateMember() throws JsonProcessingException {
		// given
		memberRepository.delete(member); // member가 DB에 없을 때 가정
		LoginRequest loginRequest = GOOGLE_LOGIN_REQUEST();

		MockServer.startOauth2GoogleServerFromLoginRequest(loginRequest);

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(loginRequest)
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

	@DisplayName("멤버가 DB에 있을때, LoginRequest 통한 로그인 성공")
	@Test
	void Given_LoginRequest_When_login_Then_Return_HttpStatus_OK() throws JsonProcessingException {
		// given
		LoginRequest loginRequest = GOOGLE_LOGIN_REQUEST();

		MockServer.startOauth2GoogleServerFromLoginRequest(loginRequest);

		// when
		ExtractableResponse<Response> response =
			given()
				.log()
				.all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(loginRequest)
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
