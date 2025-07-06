package com.kobi.elearning.service;

import com.kobi.elearning.dto.request.AuthCreateRequest;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.mapper.UserMapper;
import com.kobi.elearning.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    public UserResponse createUser(AuthCreateRequest request) {
        if (userRepository.existsByUserName((request.getUserName()))) {
            log.error("User with username {} already exists", request.getUserName());
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        request.setPassWord(passwordEncoder.encode(request.getPassWord()));
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(request)));
    }
}
