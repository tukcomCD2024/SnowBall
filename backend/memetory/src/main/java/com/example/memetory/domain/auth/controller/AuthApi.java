package com.example.memetory.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.memetory.domain.auth.dto.LoginRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Auth")
public interface AuthApi {

	@Operation(
		summary = "로그인",
		description = "앱에서 받아온 인증서버의 access_token을 통해서 우리 서버의 JWT를 받아가는 과정"
			+ "\n처음 로그인 할 경우 DB에 사용자 등록이 진행된다."
			+ "\nProvider에서 Provider의 Id, 사용자 프로필 이미지, 사용자의 이름을 가져온다."
	)
	@ApiResponses(
		@ApiResponse(
			responseCode = "200",
			description = "로그인 성공, header의 Authorization과 Authorization-refresh를 확인"
		)
	)
	ResponseEntity<HttpStatus> login(LoginRequest request,
		@Parameter(hidden = true) HttpServletResponse response);
}
