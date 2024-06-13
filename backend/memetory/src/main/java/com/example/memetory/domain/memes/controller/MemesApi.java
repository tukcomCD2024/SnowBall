package com.example.memetory.domain.memes.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.global.response.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Meme`s")
public interface MemesApi {

	@Operation(
		summary = "meme`s 생성",
		description = "사용자가 만든 밈을 공유할 수 있는 밈스 생성",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "밈스 생성!"
		)
	})
	ResponseEntity<ResultResponse> registerMemes(
		@Parameter(hidden = true) String email,
		GenerateMemesRequest generateMemesRequest
	);

	@Operation(
		summary = "meme`s 삭제",
		description = "meme`s 삭제",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "밈스 삭제"
		)
	})
	ResponseEntity<ResultResponse> deleteMemes(
		@Parameter(hidden = true) String email,
		@Parameter(in = ParameterIn.PATH, description = "밈스 아이디", required = true) Long memesId
	);

	@Operation(
		summary = "meme`s 단일 조회",
		description = "밈스의 상세정보 조회(밈스를 보기위해 클릭하면 나오는 페이지에 필요한 정보들)",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "밈스 단일 조회"
		)
	})
	ResponseEntity<ResultResponse> findMemesResponse(
		@Parameter(in = ParameterIn.PATH, description = "밈스 아이디", required = true) Long memesId
	);

	@Operation(
		summary = "meme`s 전체 조회",
		description = "밈스 전체 조회 (유튜브 홈 화면과 같은 무한 스크롤)",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "밈스 전체 조회"
		)
	})
	ResponseEntity<ResultResponse> findMemesInfoSliceResponse(Pageable pageable);
}
