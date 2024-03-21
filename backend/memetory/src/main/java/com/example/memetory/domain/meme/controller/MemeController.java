package com.example.memetory.domain.meme.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		String aiServerSendDto = memeService.getAIServerSendJson(memeServiceDto);



		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
