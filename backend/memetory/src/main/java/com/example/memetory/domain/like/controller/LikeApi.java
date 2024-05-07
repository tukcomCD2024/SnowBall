package com.example.memetory.domain.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import com.example.memetory.global.response.ResultResponse;

@Tag(name = "Like")
public interface LikeApi {

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
	ResponseEntity<ResultResponse> register(
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
    ResponseEntity<ResultResponse> cancel(
            @Parameter(hidden = true) String email,
            @Parameter(in = ParameterIn.PATH, description = "밈스 아이디", required = true)
            Long memesId
    );
}
