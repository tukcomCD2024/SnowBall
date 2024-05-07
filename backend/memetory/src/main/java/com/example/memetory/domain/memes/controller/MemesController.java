package com.example.memetory.domain.memes.controller;

import static com.example.memetory.global.response.ResultCode.*;

import com.example.memetory.domain.memes.dto.MemesServiceDto;
import com.example.memetory.domain.memes.dto.request.GenerateMemesRequest;
import com.example.memetory.domain.memes.service.MemesService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/memes")
public class MemesController implements MemesApi{
    private final MemesService memesService;

    @PostMapping
    @Override
    public ResponseEntity<ResultResponse> register(@LoginMemberEmail String email, @RequestBody GenerateMemesRequest generateMemesRequest) {
        MemesServiceDto memesServiceDto = generateMemesRequest.toServiceDto(email);
        memesService.register(memesServiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_MEMES_SUCCESS));
    }
}
