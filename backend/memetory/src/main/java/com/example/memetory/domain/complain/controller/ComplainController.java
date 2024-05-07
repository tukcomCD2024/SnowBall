package com.example.memetory.domain.complain.controller;

import static com.example.memetory.global.response.ResultCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.complain.dto.ComplainServiceDto;
import com.example.memetory.domain.complain.dto.request.GenerateComplainRequest;
import com.example.memetory.domain.complain.service.ComplainService;
import com.example.memetory.global.annotation.LoginMemberEmail;
import com.example.memetory.global.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complains")
public class ComplainController implements ComplainApi {
	private final ComplainService complainService;

	@PostMapping
	@Override
	public ResponseEntity<ResultResponse> register(@LoginMemberEmail String email,
		@RequestBody GenerateComplainRequest generateComplainRequest) {
		ComplainServiceDto newComplainServiceDto = generateComplainRequest.toServiceDto(email);
		complainService.register(newComplainServiceDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(CREATE_COMPLAIN_SUCCESS));
	}

	@DeleteMapping("/{complainId}")
	@Override
	public ResponseEntity<ResultResponse> delete(@PathVariable Long complainId) {
		ComplainServiceDto newComplainServiceDto = ComplainServiceDto.create(complainId);
		complainService.delete(newComplainServiceDto);

		return ResponseEntity.ok(ResultResponse.of(DELETE_COMMENT_SUCCESS));
	}
}
