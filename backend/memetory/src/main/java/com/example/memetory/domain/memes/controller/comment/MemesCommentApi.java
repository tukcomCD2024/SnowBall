package com.example.memetory.domain.memes.controller.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.memetory.domain.comment.dto.request.CommentRequest;
import com.example.memetory.global.response.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment")
public interface MemesCommentApi {

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
	ResponseEntity<ResultResponse> registerComment(
		@Parameter(hidden = true) String email,
		@Parameter(in = ParameterIn.PATH, description = "밈스 아이디", required = true) Long memesId,
		CommentRequest commentRequest
	);

	@Operation(
		summary = "밈스 댓글 삭제",
		description = "댓글을 삭제 한다.",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "댓글 삭제!"
		)
	})
	ResponseEntity<ResultResponse> deleteComment(
		@Parameter(hidden = true) String email,
		@Parameter(in = ParameterIn.PATH, description = "댓글 아이디", required = true) Long commentId
	);

	@Operation(
		summary = "전체 밈스 댓글 조회",
		description = "전체 밈스 댓글을 조회한다.",
		security = {@SecurityRequirement(name = "access_token")}
	)
	@GetMapping()
	ResponseEntity<ResultResponse> findCommentInfoSlice(
		@Parameter(in = ParameterIn.PATH, description = "댓글 아이디", required = true) Long memesId,
		Pageable pageable);
}