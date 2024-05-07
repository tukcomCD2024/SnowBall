package com.example.memetory.domain.comment.controller;

import static com.example.memetory.global.response.ResultCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.comment.dto.CommentServiceDto;
import com.example.memetory.domain.comment.dto.request.GenerateCommentRequest;
import com.example.memetory.domain.comment.service.CommentService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController implements CommentApi {

	private final CommentService commentService;

	@PostMapping
	@Override
	public ResponseEntity<ResultResponse> register(@LoginMemberEmail String email,
		@RequestBody GenerateCommentRequest generateCommentRequest) {
		CommentServiceDto newCommentServiceDto = generateCommentRequest.toServiceDto(email);
		commentService.register(newCommentServiceDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_COMMENT_SUCCESS));
	}

	@DeleteMapping("/{commentId}")
	@Override
	public ResponseEntity<ResultResponse> delete(@PathVariable Long commentId) {
		CommentServiceDto newCommentServiceDto = CommentServiceDto.create(commentId);
		commentService.delete(newCommentServiceDto);

		return ResponseEntity.ok(ResultResponse.of(DELETE_COMMENT_SUCCESS));
	}
}
