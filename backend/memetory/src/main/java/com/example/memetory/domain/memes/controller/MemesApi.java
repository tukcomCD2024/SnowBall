package com.example.memetory.domain.memes.controller;

import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    ResponseEntity<HttpStatus> register(
            @Parameter(hidden = true) String email,
            GenerateMemesRequest generateMemesRequest
    );

    @Operation(
            summary = "meme`s 인기차트 조회",
            description = "좋아요 수 많은 순으로 10개 조회",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인기차트 조회!"
            )
    })
    ResponseEntity<MemesListResponse> findTopTenMemesByLike();

    @Operation(
            summary = "meme`s 이달의 인기차트 조회",
            description = "최근 한 달 동안 좋아요 수 많은 순으로 10개 조회",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이달의 인기차트 조회!"
            )
    })
    ResponseEntity<MemesListResponse> findTopTenMemesByLikeForMonth();
}
