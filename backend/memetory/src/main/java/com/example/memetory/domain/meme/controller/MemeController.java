package com.example.memetory.domain.meme.controller;

import org.springframework.beans.factory.annotation.Value;
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

import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.meme.dto.GenerateMemeListRequest;
import com.example.memetory.domain.meme.dto.MemeListResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
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

	@PostMapping("/create/{memberId}")
	public ResponseEntity<HttpStatus> callBackMeme(@PathVariable Long memberId,
		@RequestBody ShotStackCallBackRequest shotStackCallBackRequest) {

		MemeServiceDto memeServiceDto = shotStackCallBackRequest.toServiceDto(memberId);

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
			.subscribe();

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/{memeId}")
	public ResponseEntity<MemeResponse> findMeme(@LoginMemberEmail String email, @PathVariable Long memeId) {
		MemeServiceDto memeServiceDto = MemeServiceDto.create(email, memeId);

		if (memeService.checkMember(memeServiceDto)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memeService.getMeme(memeServiceDto));
	}

	@GetMapping
	public ResponseEntity<MemeListResponse> findMemeList(@LoginMemberEmail String email) {
		MemeServiceDto memeServiceDto = MemeServiceDto.create(email);

		MemeListResponse memeListResponse = memeService.getAllMeme(memeServiceDto);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memeListResponse);
	}
}
