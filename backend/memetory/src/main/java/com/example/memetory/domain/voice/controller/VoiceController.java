package com.example.memetory.domain.voice.controller;

import com.example.memetory.domain.voice.dto.VoiceServiceDto;
import com.example.memetory.domain.voice.dto.request.GenerateVoiceDto;
import com.example.memetory.domain.voice.service.VoiceService;
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
@RequestMapping("/voice")
public class VoiceController implements VoiceApi{

    private final VoiceService voiceService;

    @PostMapping
    @Override
    public ResponseEntity<HttpStatus> register(@LoginMemberEmail String email, @RequestBody GenerateVoiceDto generateVoiceDto) {
        VoiceServiceDto voiceServiceDto = generateVoiceDto.toServiceDto(email);
        voiceService.register(voiceServiceDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
