package com.example.memetory.domain.comment.controller;

import com.example.memetory.domain.comment.dto.request.GenerateCommentRequest;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag(name = "Comment")
public interface CommentApi {

    @Operation(
            summary = "밈스 댓글 생성",
            description = "밈스에 댓글을 달 수 있다.",
            security = {@SecurityRequirement(name = "access_token")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 생성!"
            )
    })
    ResponseEntity<HttpStatus> register(
            @Parameter(hidden = true) String email,
            GenerateCommentRequest generateCommentRequest
    );
}
