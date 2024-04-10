package com.example.memetory.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.memetory.domain.auth.dto.LoginRequest;
import com.example.memetory.domain.auth.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<HttpStatus> login(@RequestBody LoginRequest request, HttpServletResponse response) {
		authService.authenticateOrRegisterUser(request, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
