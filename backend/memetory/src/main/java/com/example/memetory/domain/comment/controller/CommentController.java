package com.example.memetory.domain.comment.controller;

import com.example.memetory.domain.comment.dto.CommentServiceDto;
import com.example.memetory.domain.comment.dto.request.DeleteCommentRequest;
import com.example.memetory.domain.comment.dto.request.GenerateCommentRequest;
import com.example.memetory.domain.comment.service.CommentService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController implements CommentApi{

    private final CommentService commentService;

    @PostMapping
    @Override
    public ResponseEntity<HttpStatus> register(@LoginMemberEmail String email, @RequestBody GenerateCommentRequest generateCommentRequest) {
        CommentServiceDto newCommentServiceDto = generateCommentRequest.toServiceDto(email);
        commentService.register(newCommentServiceDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{commentId}")
    @Override
    public ResponseEntity<HttpStatus> delete(@PathVariable Long commentId, @RequestBody DeleteCommentRequest deleteCommentRequest) {
        CommentServiceDto newCommentServiceDto = deleteCommentRequest.toServiceDto(commentId);
        commentService.delete(newCommentServiceDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
