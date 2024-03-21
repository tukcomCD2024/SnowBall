package com.example.memetory.domain.meme.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.memetory.domain.meme.dto.GenerateMemeListRequest;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.dto.ShotStackCallBackRequest;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.global.annotation.LoginMemberEmail;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meme")
public class MemeController {
	private final MemeService memeService;

	@Value("${spring.ai-server.url}")
	private String AI_SERVER_URL;

	@PostMapping("/create/{id}")
	public ResponseEntity<HttpStatus> callBackMeme(@PathVariable Long id,
		@RequestBody ShotStackCallBackRequest shotStackCallBackRequest) {

		MemeServiceDto memeServiceDto = shotStackCallBackRequest.toServiceDto(id);

		memeService.register(memeServiceDto);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping
	public ResponseEntity<HttpStatus> register(@LoginMemberEmail String email,
		@RequestBody GenerateMemeListRequest generateMemeListRequest) {

		MemeServiceDto memeServiceDto = generateMemeListRequest.toServiceDto(email);
		String aiServerSendJson = memeService.getAIServerSendJson(memeServiceDto);

		WebClient
			.create(AI_SERVER_URL)
			.post()
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(aiServerSendJson))
			.retrieve()
			.bodyToMono(Void.class)
			.block(); // block 해야만 이미지가 전송되는 이유 알아보기

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
