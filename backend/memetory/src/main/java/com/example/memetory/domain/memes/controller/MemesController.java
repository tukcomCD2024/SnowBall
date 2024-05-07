package com.example.memetory.domain.memes.controller;

import static com.example.memetory.global.response.ResultCode.*;

import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.dto.response.MemesInfoListResponse;
import com.example.memetory.domain.memes.dto.response.MemesListResponse;
import com.example.memetory.domain.memes.dto.response.MemesResponse;
import com.example.memetory.domain.memes.service.MemesService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ResultResponse> register(@LoginMemberEmail String email, @RequestBody GenerateMemesRequest generateMemesRequest) {
        MemesServiceDto memesServiceDto = generateMemesRequest.toServiceDto(email);
        memesService.register(memesServiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_MEMES_SUCCESS));
    }

    // 밈스 좋아요 순으로 상위 10개 조회
    @GetMapping("/like/all")
    @Override
    public ResponseEntity<MemesInfoListResponse> findTopMemesByLike() {
        MemesInfoListResponse newMemesInfoListResponse = memesService.findTopMemesByLike();

        return ResponseEntity.status(HttpStatus.OK).body(newMemesInfoListResponse);
    }

    // 최근 한 달 동안 생성된 밈스 중 좋아요 순으로 상위 10개 조회
    @GetMapping("/like/month")
    @Override
    public ResponseEntity<MemesInfoListResponse> findTopMemesByLikeForMonth() {
        MemesInfoListResponse newMemesInfoListResponse = memesService.findTopMemesByLikeForMonth();

        return ResponseEntity.status(HttpStatus.OK).body(newMemesInfoListResponse);
    }

    // 최근 한 주 동안 생성된 밈스 중 좋아요 순으로 상위 10개 조회
    @GetMapping("/like/week")
    @Override
    public ResponseEntity<MemesInfoListResponse> findTopMemesByLikeForWeek() {
        MemesInfoListResponse newMemesInfoListResponse = memesService.findTopMemesByLikeForWeek();

        return ResponseEntity.status(HttpStatus.OK).body(newMemesInfoListResponse);
    }

    // 밈스 삭제
    @DeleteMapping("/{memesId}")
    @Override
    public ResponseEntity<HttpStatus> deleteMemes(@PathVariable Long memesId) {
        MemesServiceDto newMemesServiceDto = MemesServiceDto.create(memesId);
        memesService.delete(newMemesServiceDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 밈스 단일 조회
    @GetMapping("/{memesId}")
    @Override
    public ResponseEntity<MemesResponse> findMemes(@PathVariable Long memesId) {
        MemesServiceDto newMemesServiceDto = MemesServiceDto.create(memesId);
        MemesResponse newMemesResponse = memesService.findOne(newMemesServiceDto);

        return ResponseEntity.status(HttpStatus.OK).body(newMemesResponse);
    }

    // 밈스 전체 조회
    @GetMapping
    @Override
    public ResponseEntity<MemesListResponse> findAllMemes(Pageable pageable) {
        MemesListResponse memesListResponse = memesService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(memesListResponse);
    }
}
