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
import com.example.memetory.domain.memes.dto.response.MemesInfoListResponse;
import com.example.memetory.domain.memes.dto.response.MemesListResponse;
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

	// 밈스 생성
	@PostMapping
	@Override
	public ResponseEntity<ResultResponse> register(@LoginMemberEmail String email,
		@RequestBody GenerateMemesRequest generateMemesRequest) {
		memesService.register(generateMemesRequest.toServiceDto(email));

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_MEMES_SUCCESS));
	}

	// 밈스 좋아요 순으로 상위 10개 조회
	@GetMapping("/like/all")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLike() {
		MemesInfoListResponse response = memesService.findTopMemesByLike();

		return ResponseEntity.ok(ResultResponse.of(GET_TOP_TEN_MEMES_SUCCESS, response));
	}

	// 최근 한 달 동안 생성된 밈스 중 좋아요 순으로 상위 10개 조회
	@GetMapping("/like/month")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLikeForMonth() {
		MemesInfoListResponse response = memesService.findTopMemesByLikeForMonth();

		return ResponseEntity.ok(ResultResponse.of(GET_MONTH_TOP_TEN_MEMES_SUCCESS, response));
	}

	// 최근 한 주 동안 생성된 밈스 중 좋아요 순으로 상위 10개 조회
	@GetMapping("/like/week")
	@Override
	public ResponseEntity<ResultResponse> findTopMemesByLikeForWeek() {
		MemesInfoListResponse response = memesService.findTopMemesByLikeForWeek();

		return ResponseEntity.ok(ResultResponse.of(GET_WEEK_TOP_TEN_MEMES_SUCCESS, response));
	}

	// 밈스 삭제 -> 다른 유저가 삭제할 수 있음
	@DeleteMapping("/{memesId}")
	@Override
	public ResponseEntity<ResultResponse> deleteMemes(@PathVariable Long memesId) {
		memesService.delete(MemesServiceDto.create(memesId));

		return ResponseEntity.ok(ResultResponse.of(DELETE_MEMES_SUCCESS));
	}

	// 밈스 단일 조회? 밈스 상세 조회?
	@GetMapping("/{memesId}")
	@Override
	public ResponseEntity<ResultResponse> findMemes(@PathVariable Long memesId) {
		MemesResponse response = memesService.findOne(MemesServiceDto.create(memesId));

		return ResponseEntity.ok(ResultResponse.of(GET_ONE_MEMES_SUCCESS, response));
	}

	// 밈스 전체 조회
	@GetMapping
	@Override
	public ResponseEntity<ResultResponse> findAllMemes(Pageable pageable) {
		MemesListResponse response = memesService.findAll(pageable);

		return ResponseEntity.ok(ResultResponse.of(GET_ALL_MEMES_SUCCESS, response));
	}
}
