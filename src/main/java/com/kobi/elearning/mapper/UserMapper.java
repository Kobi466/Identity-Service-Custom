package com.kobi.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.kobi.elearning.dto.request.AuthCreateRequest;
import com.kobi.elearning.dto.request.UserUpdateRequest;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.entity.User;


@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
	User toUser(AuthCreateRequest request);

	UserResponse toUserResponse(User user);

	void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
