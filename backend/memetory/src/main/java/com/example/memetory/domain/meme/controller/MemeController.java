package com.example.memetory.domain.meme.controller;

import static com.example.memetory.global.response.ResultCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.meme.dto.GenerateMemeListRequest;
import com.example.memetory.domain.meme.dto.MemePageResponse;
import com.example.memetory.domain.meme.dto.MemeResponse;
import com.example.memetory.domain.meme.dto.MemeServiceDto;
import com.example.memetory.domain.meme.dto.ShotStackCallBackRequest;
import com.example.memetory.domain.meme.service.MemeService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meme")
public class MemeController implements MemeApi {
	private final MemeService memeService;

	@PostMapping("/create/{memberId}")
	@Override
	public ResponseEntity<HttpStatus> callBackMeme(@PathVariable Long memberId,
		@RequestBody ShotStackCallBackRequest shotStackCallBackRequest) {
		MemeServiceDto memeServiceDto = shotStackCallBackRequest.toServiceDtoFromMemberId(memberId);

		memeService.registerMeme(memeServiceDto);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping
	@Override
	public ResponseEntity<ResultResponse> registerMeme(@LoginMemberEmail String email,
		@RequestBody GenerateMemeListRequest generateMemeListRequest) {

		MemeServiceDto memeServiceDto = generateMemeListRequest.toServiceDto(email);
		memeService.sendToMemeServer(memeServiceDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_MEME_SUCCESS));
	}

	@GetMapping("/{memeId}")
	@Override
	public ResponseEntity<ResultResponse> findMemberMemeResponse(@LoginMemberEmail String email,
		@PathVariable Long memeId) {
		MemeServiceDto memeServiceDto = MemeServiceDto.fromEmailAndMemeId(email, memeId);
		MemeResponse response = memeService.findMemberMemeResponse(memeServiceDto);

		return ResponseEntity.ok(ResultResponse.of(GET_ONE_MEME_SUCCESS, response));
	}

	@GetMapping
	@Override
	public ResponseEntity<ResultResponse> findMemberMemePageResponse(@LoginMemberEmail String email,
		Pageable pageable) {
		MemeServiceDto memeServiceDto = MemeServiceDto.fromEmail(email);
		MemePageResponse response = memeService.findMemberMemePageResponse(memeServiceDto, pageable);

		return ResponseEntity.ok(ResultResponse.of(GET_MEMBER_MEME_SUCCESS, response));
	}
}
