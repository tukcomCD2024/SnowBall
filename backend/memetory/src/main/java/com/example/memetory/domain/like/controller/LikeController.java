package com.example.memetory.domain.like.controller;

import static com.example.memetory.global.response.ResultCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.service.LikeService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memes")
public class LikeController implements LikeApi {
	private final LikeService likeService;

	@PostMapping("/{memesId}/like")
	@Override
	public ResponseEntity<ResultResponse> register(@LoginMemberEmail String email, @PathVariable Long memesId) {
		LikeServiceDto likeServiceDto = LikeServiceDto.create(email, memesId);
		likeService.register(likeServiceDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_LIKE_SUCCESS));
	}

	@DeleteMapping("/{memesId}/like")
	@Override
	public ResponseEntity<ResultResponse> cancel(@LoginMemberEmail String email, @PathVariable Long memesId) {
		LikeServiceDto likeServiceDto = LikeServiceDto.create(email, memesId);
		likeService.cancel(likeServiceDto);

		return ResponseEntity.ok(ResultResponse.of(DELETE_LIKE_SUCCESS));
	}
}
