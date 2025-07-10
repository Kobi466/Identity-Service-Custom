package com.kobi.elearning.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kobi.elearning.dto.request.*;
import com.kobi.elearning.dto.response.*;
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
	ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
		return ApiResponse.ok(authenticationService.authenticate(request), SuccessCode.LOGIN_SUCCESS);
	}

	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
		return ApiResponse.ok(authenticationService.introspect(request), SuccessCode.INTROSPECT_SUCCESS);
	}

	@PostMapping("/refresh")
	ApiResponse<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
		return ApiResponse.ok(authenticationService.refreshToken(request), SuccessCode.REFRESH_TOKEN_SUCCESS);
	}

	@PostMapping("/logout")
	ApiResponse<Void> logout( @Valid @RequestBody LogoutRequest request) {
		authenticationService.logout(request);
		return ApiResponse.ok(null, SuccessCode.LOGOUT_SUCCESS);
	}
}
