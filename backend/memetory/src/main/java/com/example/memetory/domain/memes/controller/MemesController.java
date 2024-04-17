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

    @PostMapping
    @Override
    public ResponseEntity<HttpStatus> register(@LoginMemberEmail String email, @RequestBody GenerateMemesRequest generateMemesRequest) {
        MemesServiceDto memesServiceDto = generateMemesRequest.toServiceDto(email);
        memesService.register(memesServiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/likeAll")
    @Override
    public ResponseEntity<MemesListResponse> findTopTenMemesByLike() {
        MemesListResponse newMemesListResponse = memesService.LikeTopTen();

        return ResponseEntity.status(HttpStatus.OK).body(newMemesListResponse);
    }
}
