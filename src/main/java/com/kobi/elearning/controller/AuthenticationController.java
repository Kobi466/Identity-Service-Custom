package com.kobi.elearning.controller;

import com.kobi.elearning.dto.request.AuthCreateRequest;
import com.kobi.elearning.dto.response.ApiResponse;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.exception.SuccessCode;
import com.kobi.elearning.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping
    ApiResponse<UserResponse> register(@Valid @RequestBody AuthCreateRequest request) {
        return ApiResponse.ok(authenticationService.createUser(request), SuccessCode.USER_CREATED);
    }
}
