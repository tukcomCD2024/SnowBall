package com.example.memetory.domain.memes.controller;

import static com.example.memetory.global.response.ResultCode.*;

import java.util.List;

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

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.service.LikeService;
import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesInfo;
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
	private final LikeService likeService;

	@PostMapping
	@Override
	public ResponseEntity<ResultResponse> register(@LoginMemberEmail String email,
		@RequestBody GenerateMemesRequest generateMemesRequest) {
		memesService.register(generateMemesRequest.toServiceDto(email));

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_MEMES_SUCCESS));
	}

	@GetMapping("/like/all")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLike() {
		return ResponseEntity.ok(ResultResponse.of(GET_TOP_TEN_MEMES_SUCCESS, memesService.getTopMemesByLike()));
	}

	@GetMapping("/like/month")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLikeForMonth() {
		List<MemesInfo> response = memesService.getTopMemesByLikeForMonth();

		return ResponseEntity.ok(ResultResponse.of(GET_MONTH_TOP_TEN_MEMES_SUCCESS, response));
	}

	@GetMapping("/like/week")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLikeForWeek() {
		List<MemesInfo> response = memesService.getTopMemesByLikeForWeek();

		return ResponseEntity.ok(ResultResponse.of(GET_WEEK_TOP_TEN_MEMES_SUCCESS, response));
	}

	@DeleteMapping("/{memesId}")
	@Override
	public ResponseEntity<ResultResponse> deleteMemes(@LoginMemberEmail String email, @PathVariable Long memesId) {
		memesService.delete(MemesServiceDto.create(memesId, email));
		return ResponseEntity.ok(ResultResponse.of(DELETE_MEMES_SUCCESS));
	}

	@GetMapping("/{memesId}")
	@Override
	public ResponseEntity<ResultResponse> findMemes(@PathVariable Long memesId) {
		MemesResponse response = memesService.getMemesResponse(MemesServiceDto.create(memesId));

		return ResponseEntity.ok(ResultResponse.of(GET_ONE_MEMES_SUCCESS, response));
	}

	@GetMapping
	@Override
	public ResponseEntity<ResultResponse> findAllMemes(Pageable pageable) {
		MemesInfoSliceResponse response = memesService.getMemesInfoSliceResponse(pageable);

		return ResponseEntity.ok(ResultResponse.of(GET_ALL_MEMES_SUCCESS, response));
	}

	@PostMapping("/{memesId}/like")
	@Override
	public ResponseEntity<ResultResponse> registerLike(@LoginMemberEmail String email, @PathVariable Long memesId) {
		LikeServiceDto likeServiceDto = LikeServiceDto.create(email, memesId);
		likeService.register(likeServiceDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_LIKE_SUCCESS));
	}

	@DeleteMapping("/{memesId}/like")
	@Override
	public ResponseEntity<ResultResponse> cancelLike(@LoginMemberEmail String email, @PathVariable Long memesId) {
		LikeServiceDto likeServiceDto = LikeServiceDto.create(email, memesId);
		likeService.cancel(likeServiceDto);

		return ResponseEntity.ok(ResultResponse.of(DELETE_LIKE_SUCCESS));
	}
}
