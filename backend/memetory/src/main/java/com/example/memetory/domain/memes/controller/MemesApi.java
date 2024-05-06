package com.example.memetory.domain.memes.controller;

import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesInfoListResponse;
import com.example.memetory.domain.memes.dto.response.MemesListResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
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
    ResponseEntity<MemesInfoListResponse> findTopMemesByLike();

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
    ResponseEntity<MemesInfoListResponse> findTopMemesByLikeForMonth();

    @Operation(
            summary = "meme`s 이주의 인기차트 조회",
            description = "최근 한 주 동안 좋아요 수 많은 순으로 10개 조회",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이주의 인기차트 조회!"
            )
    })
    ResponseEntity<MemesInfoListResponse> findTopMemesByLikeForWeek();

    @Operation(
            summary = "meme`s 삭제",
            description = "meme`s 삭제",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "밈스 삭제!"
            )
    })
    ResponseEntity<HttpStatus> delete(
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
                    description = "밈스 단일 조회!"
            )
    })
    ResponseEntity<MemesResponse> findOne(
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
                    description = "밈스 전체 조회!"
            )
    })
    ResponseEntity<MemesListResponse> findAll(Pageable pageable);
}
