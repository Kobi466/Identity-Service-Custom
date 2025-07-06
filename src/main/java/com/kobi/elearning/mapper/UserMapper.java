package com.kobi.elearning.mapper;

import com.kobi.elearning.dto.request.UserUpdateRequest;
import com.kobi.elearning.dto.response.UserResponse;
import org.mapstruct.Mapper;

import com.kobi.elearning.dto.request.AuthCreateRequest;
import com.kobi.elearning.entity.User;
import org.mapstruct.MappingTarget;


@Mapper (componentModel = "spring")
public interface UserMapper {
	User toUser(AuthCreateRequest request);

	UserResponse toUserResponse(User user);

	void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
