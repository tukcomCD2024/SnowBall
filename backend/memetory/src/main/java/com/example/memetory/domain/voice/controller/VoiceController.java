package com.example.memetory.domain.voice.controller;

import com.example.memetory.domain.voice.dto.VoiceServiceDto;
import com.example.memetory.domain.voice.dto.request.GenerateVoiceRequestDto;
import com.example.memetory.domain.voice.dto.response.ElevenlabsVoiceLibraryResponse;
import com.example.memetory.domain.voice.dto.response.ElevenlabsVoiceResponse;
import com.example.memetory.domain.voice.dto.response.GenerateVoiceResponseDto;
import com.example.memetory.domain.voice.exception.AlreadyExistVoiceException;
import com.example.memetory.domain.voice.service.VoiceService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultCode;
import com.example.memetory.global.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
import java.rmi.AlreadyBoundException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/voice")
public class VoiceController implements VoiceApi {

    private final VoiceService voiceService;

    @Value("${elevenlabs.api.url.add}")
    private String addApiUrl;

    @Value("${elevenlabs.api.url.get}")
    private String getApiUrl;

    @Value("${elevenlabs.api.url.library}")
    private String getLibraryApiUrl;

    @Value("${elevenlabs.api.key}")
    private String apiKey;

    // 보이스 추출 및 생성
    @PostMapping
    @Override
    public ResponseEntity<ResultResponse> register(@LoginMemberEmail String email, @RequestBody GenerateVoiceRequestDto generateVoiceRequestDto) throws IOException, AlreadyBoundException {

        // s3 url을 받아서 음성파일을 가져오는 ServiceDto 로직 1개
        VoiceServiceDto voiceServiceDtoS3 = generateVoiceRequestDto.toServiceDtoS3(email);

        // 보이스가 이미 존재하는지 체크
        voiceService.isExistVoice(voiceServiceDtoS3);
        MultiValueMap<String, Object> formData = voiceService.generateVoice(voiceServiceDtoS3);

        return WebClient
                .create(addApiUrl)
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
                    return Mono.just(ResponseEntity.ok(ResultResponse.of(ResultCode.CREATE_VOICE_SUCCESS)));
                })
                .onErrorResume(error -> {
                    System.out.println(("An error occurred while processing the request: {}" + error.getMessage()));
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .block();
    }

    // 멤버별 보이스 조회
    @GetMapping("/member")
    @Override
    public ResponseEntity<ResultResponse> findByMemberId(@LoginMemberEmail String email) throws IOException {
        VoiceServiceDto voiceServiceDto = VoiceServiceDto.create(email);
        String voiceId = voiceService.findVoiceByMemberId(voiceServiceDto);

        return WebClient
                .create(getApiUrl + "/" + voiceId)
                .get()
                .header("xi-api-key", apiKey)
                .retrieve()
                .bodyToMono(ElevenlabsVoiceResponse.class)
                .flatMap(response -> {
                    // elevenlabs API 호출이 완료 되면 실행할 로직
                    return Mono.just(ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MEMBER_VOICE_SUCCESS, response)));
                })
                .onErrorResume(error -> {
                    System.out.println(("An error occurred while processing the request: {}" + error.getMessage()));
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .block();
    }

    // 기본 목소리 라이브러리 조회
    @GetMapping("/library")
    @Override
    public ResponseEntity<ResultResponse> getVoiceLibrary() throws IOException {
        return WebClient
                .create(getLibraryApiUrl)
                .get()
                .header("xi-api-key", apiKey)
                .retrieve()
                .bodyToMono(ElevenlabsVoiceLibraryResponse.class)
                .flatMap(response -> {
                    // elevenlabs API 호출이 완료 되면 실행할 로직
                    return Mono.just(ResponseEntity.ok(ResultResponse.of(ResultCode.GET_VOICE_LIBRARY_SUCCESS, response)));
                })
                .onErrorResume(error -> {
                    System.out.println(("An error occurred while processing the request: {}" + error.getMessage()));
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .block();
    }
}
