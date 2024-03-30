package com.example.memetory.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.dto.MemberSignUpRequest;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.annotation.LoginMemberEmail;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Override
    public ResponseEntity<HttpStatus> register(@RequestBody MemberSignUpRequest memberSignUpRequest,
                                               @LoginMemberEmail String email) {
        MemberServiceDto memberServiceDto = memberSignUpRequest.toServiceDto(email);

        memberService.register(memberServiceDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
