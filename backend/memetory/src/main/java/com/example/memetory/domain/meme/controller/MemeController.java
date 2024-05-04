package com.example.memetory.domain.meme.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.memetory.domain.meme.dto.GenerateMemeListRequest;
import com.example.memetory.domain.meme.dto.MemePageResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.dto.ShotStackCallBackRequest;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.global.annotation.LoginMemberEmail;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meme")
public class MemeController implements MemeApi {
	private final MemeService memeService;

	@Value("${spring.ai-server.url}")
	private String AI_SERVER_URL;

	@PostMapping("/create/{memberId}")
	@Override
	public ResponseEntity<HttpStatus> callBackMeme(@PathVariable Long memberId,
		@RequestBody ShotStackCallBackRequest shotStackCallBackRequest) {

		MemeServiceDto memeServiceDto = shotStackCallBackRequest.toServiceDto(memberId);

		memeService.register(memeServiceDto);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping
	@Override
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
			.subscribe();

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{memeId}")
	@Override
	public ResponseEntity<MemeResponse> findMeme(@LoginMemberEmail String email, @PathVariable Long memeId) {
		MemeServiceDto memeServiceDto = MemeServiceDto.create(email, memeId);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memeService.getMeme(memeServiceDto));
	}

	@GetMapping
	@Override
	public ResponseEntity<MemePageResponse> findMemeList(@LoginMemberEmail String email, Pageable pageable) {
		MemeServiceDto memeServiceDto = MemeServiceDto.create(email);

		MemePageResponse memePageResponse = memeService.getAllMeme(memeServiceDto, pageable);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memePageResponse);
	}
}
