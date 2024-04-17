package com.example.memetory.domain.complain.controller;

import com.example.memetory.domain.complain.dto.ComplainServiceDto;
import com.example.memetory.domain.complain.dto.request.GenerateComplainRequest;
import com.example.memetory.domain.complain.service.ComplainService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{complainId}")
    @Override
    public ResponseEntity<HttpStatus> delete(@PathVariable Long complainId) {
        ComplainServiceDto newComplainServiceDto = ComplainServiceDto.create(complainId);
        complainService.delete(newComplainServiceDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
