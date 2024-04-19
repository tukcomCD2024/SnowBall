package com.example.memetory.domain.memes.controller;

import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesListResponse;
import com.example.memetory.domain.memes.service.MemesService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/memes")
public class MemesController implements MemesApi{
    private final MemesService memesService;

    // 밈스 생성
    @PostMapping
    @Override
    public ResponseEntity<HttpStatus> register(@LoginMemberEmail String email, @RequestBody GenerateMemesRequest generateMemesRequest) {
        MemesServiceDto memesServiceDto = generateMemesRequest.toServiceDto(email);
        memesService.register(memesServiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 밈스 좋아요 순으로 상위 10개 조회
    @GetMapping("/likeAll")
    @Override
    public ResponseEntity<MemesListResponse> findTopMemesByLike() {
        MemesListResponse newMemesListResponse = memesService.findTopMemesByLike();

        return ResponseEntity.status(HttpStatus.OK).body(newMemesListResponse);
    }

    // 최근 한 달 동안 생성된 밈스 중 좋아요 순으로 상위 10개 조회
    @GetMapping("/likeMonth")
    @Override
    public ResponseEntity<MemesListResponse> findTopMemesByLikeForMonth() {
        MemesListResponse newMemesListResponse = memesService.findTopMemesByLikeForMonth();

        return ResponseEntity.status(HttpStatus.OK).body(newMemesListResponse);
    }

    // 최근 한 주 동안 생성된 밈스 중 좋아요 순으로 상위 10개 조회
    @GetMapping("/likeWeek")
    @Override
    public ResponseEntity<MemesListResponse> findTopMemesByLikeForWeek() {
        MemesListResponse newMemesListResponse = memesService.findTopMemesByLikeForWeek();

        return ResponseEntity.status(HttpStatus.OK).body(newMemesListResponse);
    }
}
