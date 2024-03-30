package com.example.memetory.domain.memes.controller;

import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
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
}
