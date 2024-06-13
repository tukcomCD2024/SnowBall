package com.example.memetory.domain.memes.controller;

import static com.example.memetory.global.response.ResultCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesInfoSliceResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.service.MemesService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memes")
public class MemesController implements MemesApi {
	private final MemesService memesService;

	@GetMapping
	@Override
	public ResponseEntity<ResultResponse> findMemesInfoSliceResponse(Pageable pageable) {
		MemesInfoSliceResponse response = memesService.findMemesInfoSliceResponse(pageable);

		return ResponseEntity.ok(ResultResponse.of(GET_ALL_MEMES_SUCCESS, response));
	}

	@PostMapping
	@Override
	public ResponseEntity<ResultResponse> registerMemes(@LoginMemberEmail String email,
		@RequestBody GenerateMemesRequest generateMemesRequest) {
		MemesResponse response = memesService.registerMemes(generateMemesRequest.toServiceDtoFromEmail(email));

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_MEMES_SUCCESS, response));
	}

	@GetMapping("/{memesId}")
	@Override
	public ResponseEntity<ResultResponse> findMemesResponse(@PathVariable Long memesId) {
		MemesResponse response = memesService.findMemesResponse(MemesServiceDto.fromMemesId(memesId));

		return ResponseEntity.ok(ResultResponse.of(GET_ONE_MEMES_SUCCESS, response));
	}

	@DeleteMapping("/{memesId}")
	@Override
	public ResponseEntity<ResultResponse> deleteMemes(@LoginMemberEmail String email, @PathVariable Long memesId) {
		MemesServiceDto memesServiceDto = MemesServiceDto.fromMemesIdAndEmail(memesId, email);

		memesService.deleteMemes(memesServiceDto);
		return ResponseEntity.ok(ResultResponse.of(DELETE_MEMES_SUCCESS));
	}
}
