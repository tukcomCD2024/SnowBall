package com.example.memetory.domain.voice.controller;

import com.example.memetory.domain.voice.dto.VoiceServiceDto;
import com.example.memetory.domain.voice.dto.request.GenerateVoiceRequestDto;
import com.example.memetory.domain.voice.dto.response.GenerateVoiceResponseDto;
import com.example.memetory.domain.voice.service.VoiceService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/voice")
public class VoiceController implements VoiceApi {

    private final VoiceService voiceService;

    @Value("${elevenlabs.api.url.add}")
    private String apiUrl;

    @Value("${elevenlabs.api.key}")
    private String apiKey;

    @PostMapping
    @Override
    public ResponseEntity<Object> register(@LoginMemberEmail String email, @RequestBody GenerateVoiceRequestDto generateVoiceRequestDto) throws IOException {

        // s3 url을 받아서 음성파일을 가져오는 ServiceDto 로직 1개
        VoiceServiceDto voiceServiceDtoS3 = generateVoiceRequestDto.toServiceDtoS3(email);
        MultiValueMap<String, Object> formData = voiceService.generateVoice(voiceServiceDtoS3);

        return WebClient
                .create(apiUrl)
                .post()
                .header("xi-api-key", apiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(GenerateVoiceResponseDto.class)
                .flatMap(response -> {
                    // elevenlabs API 호출이 완료 되면 실행할 로직
                    VoiceServiceDto voiceServiceDtoElevenlabs =
                            generateVoiceRequestDto.toServiceDtoElevenlabs(email, response.getElevenlabsVoiceId());
                    voiceService.register(voiceServiceDtoElevenlabs);
                    return Mono.just(ResponseEntity.status(HttpStatus.CREATED).build());
                })
                .onErrorResume(error -> {
                    System.out.println(("An error occurred while processing the request: {}" + error.getMessage()));
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .block();
    }
}
