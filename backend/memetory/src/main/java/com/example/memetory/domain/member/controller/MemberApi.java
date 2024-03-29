package com.example.memetory.domain.member.controller;

import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member")
public interface MemberApi {

    @Operation(
            summary = "회원가입",
            description = "첫 소셜로그인 후 추가 정보 기입",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공!"
            )
    })
    ResponseEntity<HttpStatus> register(
            MemberSignUpRequest memberSignUpRequest,
            @Parameter(hidden = true) String email
    );
}
