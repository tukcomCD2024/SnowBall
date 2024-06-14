package com.example.memetory.domain.memes.controller.comment;

import static com.example.memetory.global.response.ResultCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.comment.dto.CommentInfo;
import com.example.memetory.domain.comment.dto.request.CommentRequest;
import com.example.memetory.domain.comment.service.CommentService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memes/{memesId}/comments")
public class MemesCommentController implements MemesCommentApi {
	private final CommentService commentService;

	@PostMapping
	@Override
	public ResponseEntity<ResultResponse> registerComment(
		@LoginMemberEmail String email,
		@PathVariable Long memesId,
		@RequestBody CommentRequest commentRequest
	) {
		commentRequest.setMemesIdAndEmail(memesId, email);
		CommentInfo response = commentService.saveComment(commentRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_COMMENT_SUCCESS, response));
	}

	@DeleteMapping("/{commentId}")
	@Override
	public ResponseEntity<ResultResponse> deleteComment(@LoginMemberEmail String email, @PathVariable Long commentId) {
		commentService.deleteComment(email, commentId);

		return ResponseEntity.ok(ResultResponse.of(DELETE_COMMENT_SUCCESS));
	}

	//TODO 조회(슬라이스 형식)

}
