package com.example.memetory.domain.meme.controller;

import com.example.memetory.domain.meme.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag(name = "Meme")
public interface MemeApi {

    @Operation(
            summary = "밈 생성 콜백",
            description = "밈 최종 생성 후 웹 훅을 받을 api"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "콜백 성공!"
            )
    })
    ResponseEntity<HttpStatus> callBackMeme(
            @Parameter(in = ParameterIn.PATH, description = "멤버 아이디", required = true) Long memberId,
            ShotStackCallBackRequest shotStackCallBackRequest
    );

    @Operation(
            summary = "밈 생성",
            description = "템플릿, 이미지, 목소리, 대사를 입력 하고 밈을 생성한다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "밈 생성"
            )
    })
    ResponseEntity<HttpStatus> register(
            @Parameter(hidden = true) String email,
            GenerateMemeListRequest generateMemeListRequest
    );

    @Operation(
            summary = "밈 단일 조회",
            description = "밈 아이디에 해당하는 밈을 조회한다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "밈 단일 조회"
            )
    })
    ResponseEntity<MemeResponse> findMeme(
            @Parameter(hidden = true) String email,
            @Parameter(in = ParameterIn.PATH, description = "밈 아이디", required = true) Long memeId
    );

    @Operation(
            summary = "밈 전체 조회",
            description = "회원이 생성한 밈을 전체 조회한다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "밈 전체 조회"
            )
    })
    ResponseEntity<MemeListResponse> findMemeList(
            @Parameter(hidden = true) String email
    );
}
