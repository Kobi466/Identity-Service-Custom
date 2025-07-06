package com.kobi.elearning.service;


import com.kobi.elearning.dto.request.AuthCreateRequest;
import com.kobi.elearning.dto.request.UserUpdateRequest;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.mapper.UserMapper;
import com.kobi.elearning.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



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
    PasswordEncoder passwordEncoder;
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        log.info("Service updateUser with request: {}", request);
        User user = getUser(id);
        userMapper.updateUser(user, request);
        user.setPassWord(passwordEncoder.encode(request.getPassWord()));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public User getUser(String id) {
        if(userRepository.existsById(id)) {
            return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        } else {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
