package com.kobi.elearning.service;

import org.springframework.stereotype.Service;

import com.kobi.elearning.dto.request.auth.IntrospectRequest;
import com.kobi.elearning.dto.request.auth.LoginRequest;
import com.kobi.elearning.dto.request.auth.LogoutRequest;
import com.kobi.elearning.dto.request.auth.RefreshTokenRequest;
import com.kobi.elearning.dto.response.auth.AuthenticationResponse;
import com.kobi.elearning.dto.response.auth.IntrospectResponse;
import com.kobi.elearning.dto.response.auth.RefreshTokenResponse;

@Service
public interface AuthenticationService {
	public AuthenticationResponse authenticateUser(LoginRequest loginRequest);
	public RefreshTokenResponse refreshToken(RefreshTokenRequest request);
	public IntrospectResponse introspectToken(IntrospectRequest request);
	public void revokedToken(LogoutRequest request);
}
