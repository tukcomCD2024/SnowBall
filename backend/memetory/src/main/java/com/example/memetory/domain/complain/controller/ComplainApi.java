package com.example.memetory.domain.complain.controller;

import com.example.memetory.domain.complain.dto.request.GenerateComplainRequest;
import com.example.memetory.global.response.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;

@Tag(name = "Complain")
public interface ComplainApi {
    @Operation(
            summary = "밈스 신고 생성",
            description = "밈스를 신고할 수 있다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "신고 생성!"
            )
    })
	ResponseEntity<ResultResponse> register(
            @Parameter(hidden = true) String email,
            GenerateComplainRequest generateComplainRequest
    );

    @Operation(
            summary = "밈스 신고 삭제",
            description = "신고를 삭제 한다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "신고 삭제!"
            )
    })
    ResponseEntity<ResultResponse> delete(
            @Parameter(in = ParameterIn.PATH, description = "신고 아이디", required = true) Long complainId
    );
}
