package com.example.memetory.global.integration;

import static com.example.memetory.domain.auth.AuthFixture.*;
import static com.example.memetory.domain.meme.MemeFixture.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.example.memetory.domain.auth.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

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

	public static void startMemeServerFromGenerateMemeListRequest() {
		Gson gson = new Gson();

		stubFor(post("/ai-server")
			.withRequestBody(equalToJson(gson.toJson(AI_SERVER_SEND_DTO())))
			.willReturn(aResponse()
				.withStatus(201)
			));
	}
}
