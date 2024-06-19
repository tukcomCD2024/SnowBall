package com.example.memetory.domain.voice.controller;

import com.example.memetory.domain.voice.dto.request.GenerateVoiceRequestDto;
import com.example.memetory.global.response.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.rmi.AlreadyBoundException;

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
    ResponseEntity<ResultResponse> register(
            @Parameter(hidden = true) String email,
            GenerateVoiceRequestDto generateVoiceRequestDto
    ) throws IOException, AlreadyBoundException;

    @Operation(
            summary = "멤버별 목소리 조회",
            description = "voice_id에 해당하는 목소리를 조회한다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "멤버별 목소리 조회!"
            )
    })
    ResponseEntity<ResultResponse> findByMemberId(
            @Parameter(hidden = true) String email
    );

    @Operation(
            summary = "목소리 삭제",
            description = "voice_id에 해당하는 목소리를 삭제한다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "목소리 삭제!"
            )
    })
    void deleteVoice(
            @Parameter(hidden = true) String email
    );
}
