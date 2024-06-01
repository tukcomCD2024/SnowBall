package com.example.memetory.global.integration;

import static com.example.memetory.domain.auth.AuthFixture.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.example.memetory.domain.auth.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

@WireMockTest(httpPort = 9899)
public class MockServer {
	public static void startOauth2GoogleServerFromLoginRequest(LoginRequest loginRequest) throws
		JsonProcessingException {

		stubFor(get("/google-url")
			.withHeader("Authorization", equalTo("Bearer " + loginRequest.getToken()))
			.willReturn(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody(JSON_GOOGLE_OAUTH2_RESPONSE())
			));
	}
}
