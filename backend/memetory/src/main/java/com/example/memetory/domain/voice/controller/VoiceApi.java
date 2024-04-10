package com.example.memetory.domain.voice.controller;

import com.example.memetory.domain.voice.dto.request.GenerateVoiceRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@Tag(name = "Voice")
public interface VoiceApi {

    @Operation(
            summary = "목소리 추출",
            description = "S3_URL에 있는 음성파일에서 목소리를 추출한다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "목소리 추출!"
            )
    })
    ResponseEntity<Object> register(
            @Parameter(hidden = true) String email,
            GenerateVoiceRequestDto generateVoiceRequestDto
    ) throws IOException;
}
