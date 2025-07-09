package com.kobi.elearning.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kobi.elearning.dto.request.AuthCreateRequest;
import com.kobi.elearning.dto.request.IntrospectRequest;
import com.kobi.elearning.dto.request.LoginRequest;
import com.kobi.elearning.dto.response.ApiResponse;
import com.kobi.elearning.dto.response.AuthenticationResponse;
import com.kobi.elearning.dto.response.IntrospectResponse;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.exception.SuccessCode;
import com.kobi.elearning.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	AuthenticationService authenticationService;

	@PostMapping("/register")
	ApiResponse<UserResponse> register(@Valid @RequestBody AuthCreateRequest request) {
		return ApiResponse.ok(authenticationService.createUser(request), SuccessCode.USER_CREATED);
	}

	@PostMapping("/login")
	ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest request) {
		return ApiResponse.ok(authenticationService.authenticate(request), SuccessCode.LOGIN_SUCCESS);
	}

	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
		return ApiResponse.ok(authenticationService.introspect(request), SuccessCode.INTROSPECT_SUCCESS);
	}
}
