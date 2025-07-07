package com.kobi.elearning.controller;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.kobi.elearning.dto.request.UserUpdateRequest;
import com.kobi.elearning.dto.response.ApiResponse;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.exception.SuccessCode;
import com.kobi.elearning.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
	UserService userService;
	@PutMapping("/{id}")
	public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
		return ApiResponse.ok( userService.updateUser(id, request), SuccessCode.USER_UPDATED);
	}
}
