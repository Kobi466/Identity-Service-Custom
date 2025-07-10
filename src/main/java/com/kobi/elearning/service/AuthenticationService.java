package com.kobi.elearning.service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.FlashMapManager;

import com.kobi.elearning.constant.PredefinedRole;
import com.kobi.elearning.dto.request.*;
import com.kobi.elearning.dto.response.AuthenticationResponse;
import com.kobi.elearning.dto.response.IntrospectResponse;
import com.kobi.elearning.dto.response.RefreshTokenResponse;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.entity.RefreshToken;
import com.kobi.elearning.entity.Role;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.mapper.UserMapper;
import com.kobi.elearning.repository.RefreshTokenRepository;
import com.kobi.elearning.repository.RoleRepository;
import com.kobi.elearning.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
	UserRepository userRepository;
	UserMapper userMapper;
	RoleRepository roleRepository;
	PasswordEncoder passwordEncoder;
	JwtService jwtService;
	RefreshTokenRepository refreshTokenRepository;

	public UserResponse createUser(AuthCreateRequest request) {
		if (userRepository.existsByUserName(((request.getUserName())))) {
			log.error("User with username {} already exists", request.getUserName());
			throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
		}
		request.setPassWord(passwordEncoder.encode(request.getPassWord()));
		HashSet<Role> roles = new HashSet<>();

		Role role = roleRepository.findById(PredefinedRole.STUDENT).orElseThrow();
		roles.add(role);

		var user = userMapper.toUser(request);
		user.setRoles(roles);

		return userMapper.toUserResponse(userRepository.save(user));
	}

	public AuthenticationResponse authenticate(LoginRequest request) {
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

	public IntrospectResponse introspect(IntrospectRequest request) {
		return IntrospectResponse.builder()
				.valid(jwtService.validateToken(request.getToken()))
				.build();
	}

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

	public void logout (LogoutRequest request){
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
