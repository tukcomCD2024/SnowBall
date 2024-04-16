package com.example.memetory.domain.complain.controller;

import com.example.memetory.domain.complain.dto.ComplainServiceDto;
import com.example.memetory.domain.complain.dto.request.GenerateComplainRequest;
import com.example.memetory.domain.complain.service.ComplainService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complains")
public class ComplainController implements ComplainApi{
    private final ComplainService complainService;

    @PostMapping
    @Override
    public ResponseEntity<HttpStatus> register(@LoginMemberEmail String email, @RequestBody GenerateComplainRequest generateComplainRequest) {
        ComplainServiceDto newComplainServiceDto = generateComplainRequest.toServiceDto(email);
        complainService.register(newComplainServiceDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
