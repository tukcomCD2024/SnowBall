package com.example.memetory.domain.memes.controller;

import org.springframework.http.ResponseEntity;

import com.example.memetory.global.response.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

public interface MemesLikeApi {
	@Operation(
		summary = "meme`s 인기차트 조회",
		description = "좋아요 수 많은 순으로 10개 조회",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "인기차트 조회"
		)
	})
	ResponseEntity<ResultResponse> findTopMemesByLike();

	@Operation(
		summary = "meme`s 이달의 인기차트 조회",
		description = "최근 한 달 동안 좋아요 수 많은 순으로 10개 조회",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "이달의 인기차트 조회"
		)
	})
	ResponseEntity<ResultResponse> findTopMemesByLikeForMonth();

	@Operation(
		summary = "meme`s 이주의 인기차트 조회",
		description = "최근 한 주 동안 좋아요 수 많은 순으로 10개 조회",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "이주의 인기차트 조회"
		)
	})
	ResponseEntity<ResultResponse> findTopMemesByLikeForWeek();

	@Operation(
		summary = "meme`s 좋아요 등록",
		description = "공유된 meme`s 게시물에 좋아요를 등록한다.",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "좋아요 등록!"
		)
	})
	ResponseEntity<ResultResponse> registerLike(
		@Parameter(hidden = true) String email,
		@Parameter(in = ParameterIn.PATH, description = "밈스 아이디", required = true)
		Long memesId
	);

	@Operation(
		summary = "meme`s 좋아요 취소",
		description = "공유된 meme`s 게시물에 등록된 좋아요를 취소한다.",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "좋아요 취소!"
		)
	})
	ResponseEntity<ResultResponse> cancelLike(
		@Parameter(hidden = true) String email,
		@Parameter(in = ParameterIn.PATH, description = "밈스 아이디", required = true)
		Long memesId
	);
}
