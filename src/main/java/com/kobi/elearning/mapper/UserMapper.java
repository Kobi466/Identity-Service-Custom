package com.kobi.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.kobi.elearning.dto.request.auth.AuthCreateRequest;
import com.kobi.elearning.dto.request.profile.UserUpdateRequest;
import com.kobi.elearning.dto.response.profile.UserResponse;
import com.kobi.elearning.entity.User;


@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
	User toUser(AuthCreateRequest request);

	UserResponse toUserResponse(User user);

	void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
