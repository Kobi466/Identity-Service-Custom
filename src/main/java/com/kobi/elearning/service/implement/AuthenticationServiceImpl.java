package com.kobi.elearning.service.implement;


import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kobi.elearning.dto.request.auth.IntrospectRequest;
import com.kobi.elearning.dto.request.auth.LoginRequest;
import com.kobi.elearning.dto.request.auth.LogoutRequest;
import com.kobi.elearning.dto.request.auth.RefreshTokenRequest;
import com.kobi.elearning.dto.response.auth.AuthenticationResponse;
import com.kobi.elearning.dto.response.auth.IntrospectResponse;
import com.kobi.elearning.dto.response.auth.RefreshTokenResponse;
import com.kobi.elearning.entity.RefreshToken;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.mapper.UserMapper;
import com.kobi.elearning.repository.RefreshTokenRepository;
import com.kobi.elearning.repository.RoleRepository;
import com.kobi.elearning.repository.UserRepository;
import com.kobi.elearning.service.AuthenticationService;
import com.kobi.elearning.service.JwtService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AuthenticationServiceImpl implements AuthenticationService{
	UserRepository userRepository;
	UserMapper userMapper;
	RoleRepository roleRepository;
	PasswordEncoder passwordEncoder;
	JwtService jwtService;
	RefreshTokenRepository refreshTokenRepository;

	@Override
	public AuthenticationResponse authenticateUser(LoginRequest request) {
		User user = userRepository.findByUserName(request.getUserName())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		boolean isAuthenticated = passwordEncoder.matches(request.getPassWord(), user.getPassWord());
		if (!isAuthenticated) {
			log.error("Authentication failed for user {}", request.getUserName());
			throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
		}
		String r_token = jwtService.generateRefreshToken(user);
		String idUser = jwtService.getIdFromToken(r_token);
		User oneuser = userRepository.findById(idUser)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		refreshTokenRepository.save(RefreshToken.builder()
				.user(oneuser)
				.token(r_token)
				.expiryTime(jwtService.getExpirationDateFromToken(r_token))
				.used(false)
				.revoked(false)
				.createdAt(jwtService.getIssuedAtDateFromToken(r_token))
				.build()
		);
		return AuthenticationResponse.builder()
				.accessToken(jwtService.generateAccessToken(user))
				.refreshToken(r_token)
				.expiresAt(jwtService.getExpirationDateFromToken(r_token))
				.user(userMapper.toUserResponse(user))
				.build();
	}
	@Override
	public IntrospectResponse introspectToken(IntrospectRequest request) {
		return IntrospectResponse.builder()
				.valid(jwtService.validateToken(request.getToken()))
				.build();
	}
	@Override
	public RefreshTokenResponse refreshToken (RefreshTokenRequest request) {
		boolean isValid = jwtService.validateToken(request.getToken());
		if (!isValid) {
			log.error("Invalid refresh token: {}", request.getToken());
			throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
		}
		RefreshToken reFreshToken = refreshTokenRepository.findByToken(request.getToken());
		if (reFreshToken.isUsed() || reFreshToken.isRevoked() ) {
			log.error("Refresh token has already been used or revoked: {}", request.getToken());
			throw new AppException(ErrorCode.REFRESH_TOKEN_ALREADY_USED_OR_REVOKED);
		}
		reFreshToken.setUsed(true);
		reFreshToken.setRevoked(true);
		refreshTokenRepository.save(reFreshToken);
		User user = userRepository.findById(jwtService
						.getIdFromToken(request.getToken()))
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		String newAccessToken = jwtService.generateAccessToken(user);
		String newRefreshToken = jwtService.generateRefreshToken(user);
		refreshTokenRepository.save(RefreshToken
				.builder()
				.user(reFreshToken.getUser())
				.token(newRefreshToken)
				.expiryTime(jwtService.getExpirationDateFromToken(newRefreshToken))
				.used(false)
				.revoked(false)
				.createdAt(jwtService.getIssuedAtDateFromToken(newRefreshToken))
				.build());
		return RefreshTokenResponse.builder()
				.accessTokenNew(newAccessToken)
				.refreshTokenNew(newRefreshToken)
				.accessTokenExpiration(jwtService
						.getExpirationDateFromToken(newAccessToken))
				.build();
	}

	@Override
	public void revokedToken (LogoutRequest request){
//		boolean isValid = jwtService.validateToken(request.getToken());
//		if (!isValid) {
//			log.error("Invalid access token: {}", request.getToken());
//			throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
//		}
		String userId = jwtService.getIdFromToken(request.getToken());
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		List<RefreshToken> tokenList = refreshTokenRepository.findAllByUser(user);
		tokenList.forEach(token -> {
			token.setRevoked(true);
			token.setUsed(true);
		});
		refreshTokenRepository.saveAll(tokenList);
	}
}
