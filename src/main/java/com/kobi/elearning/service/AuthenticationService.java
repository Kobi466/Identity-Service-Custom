package com.kobi.elearning.service;


import java.util.HashSet;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kobi.elearning.constant.PredefinedRole;
import com.kobi.elearning.dto.request.AuthCreateRequest;
import com.kobi.elearning.dto.request.IntrospectRequest;
import com.kobi.elearning.dto.request.LoginRequest;
import com.kobi.elearning.dto.response.AuthenticationResponse;
import com.kobi.elearning.dto.response.IntrospectResponse;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.entity.Role;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.mapper.UserMapper;
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
		var user = userRepository.findByUserName(request.getUserName())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		boolean isAuthenticated = passwordEncoder.matches(user.getPassWord(), request.getPassWord());
		if (!isAuthenticated) {
			log.error("Authentication failed for user {}", request.getUserName());
//			throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
		}
		return AuthenticationResponse.builder()
				.accessToken(jwtService.generateAccessToken(user))
				.refreshToken(jwtService.generateRefreshToken(user))
				.user(userMapper.toUserResponse(user))
				.build();
	}

	public IntrospectResponse introspect(IntrospectRequest request) {
		return IntrospectResponse.builder()
				.valid(jwtService.validateToken(request.getToken()))
				.build();
	}
}
