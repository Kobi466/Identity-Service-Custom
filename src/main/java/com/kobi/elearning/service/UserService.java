package com.kobi.elearning.service;


import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kobi.elearning.constant.PredefinedRole;
import com.kobi.elearning.dto.request.auth.AuthCreateRequest;
import com.kobi.elearning.dto.request.profile.UserUpdateRequest;
import com.kobi.elearning.dto.response.profile.UserResponse;
import com.kobi.elearning.entity.Role;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.mapper.UserMapper;
import com.kobi.elearning.repository.RoleRepository;
import com.kobi.elearning.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
	UserRepository userRepository;
	UserMapper userMapper;
	RoleRepository roleRepository;
	PasswordEncoder passwordEncoder;
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
	public UserResponse updateUser(String id, UserUpdateRequest request) {
		User user = getUser(id);
		if (userRepository.existsByUserName((request.getUserName()))) {
			log.error("User with username {} already exists", request.getUserName());
			throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
		}
		userMapper.updateUser(user, request);
		return userMapper.toUserResponse(userRepository.save(user));
	}
	public User getUser(String id) {
		if(userRepository.existsById(id)) {
			return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		} else {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('USER_VIEW')")
	public UserResponse getMyInformation() {
		var authentication = SecurityContextHolder.getContext();
		String userId = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getAuthentication().getPrincipal()).getSubject();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		return userMapper.toUserResponse(user);
	}

	public List<UserResponse> getUsers() {
		log.info("Fetching all users");
		return userRepository.findAll().stream()
				.map(userMapper::toUserResponse)
				.toList();
	}

}
