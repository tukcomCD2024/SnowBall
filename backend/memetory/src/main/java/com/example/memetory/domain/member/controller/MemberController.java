package com.example.memetory.domain.member.controller;

import static com.example.memetory.global.response.ResultCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.member.dto.MemberServiceDto;
import com.example.memetory.domain.member.dto.request.MemberUpdateRequest;
import com.example.memetory.domain.member.dto.response.MemberResponse;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberApi {
	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<ResultResponse> updateMember(@LoginMemberEmail String email,
		@RequestBody MemberUpdateRequest memberUpdateRequest) {
		MemberServiceDto memberServiceDto = memberUpdateRequest.toServiceDtoFromEmail(email);

		memberService.updateMember(memberServiceDto);

		return ResponseEntity.ok(ResultResponse.of(UPDATE_MEMBER_SUCCESS));
	}

	@GetMapping
	public ResponseEntity<ResultResponse> findMember(@LoginMemberEmail String email) {
		MemberServiceDto memberServiceDto = MemberServiceDto.createFromEmail(email);

		MemberResponse response = memberService.findMemberResponse(memberServiceDto);

		return ResponseEntity.ok(ResultResponse.of(GET_MEMBER_SUCCESS, response));
	}
}
