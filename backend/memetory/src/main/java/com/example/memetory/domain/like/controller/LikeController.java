package com.example.memetory.domain.like.controller;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.service.LikeService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{memesId}/like")
    public ResponseEntity<String> register(@LoginMemberEmail String email, @PathVariable Long memesId) {
        LikeServiceDto likeServiceDto = LikeServiceDto.create(email, memesId);
        likeService.register(likeServiceDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("좋아요 등록!!");
    }

    @DeleteMapping("/{memesId}/like")
    public ResponseEntity<String> cancel(@LoginMemberEmail String email, @PathVariable Long memesId) {
        LikeServiceDto likeServiceDto = LikeServiceDto.create(email, memesId);
        likeService.cancel(likeServiceDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("좋아요 취소!");
    }
}
