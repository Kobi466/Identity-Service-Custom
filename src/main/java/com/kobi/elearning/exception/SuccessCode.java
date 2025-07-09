package com.kobi.elearning.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	USER_CREATED(1001, "User created successfully", HttpStatus.CREATED),
	USER_UPDATED(1002, "User updated successfully", HttpStatus.OK),
	LOGIN_SUCCESS(1003, "Login successful", HttpStatus.OK),
	INTROSPECT_SUCCESS(1004, "Introspection successful", HttpStatus.OK),
	USER_FETCH_SUCCESS(1005, "User fetched successfully", HttpStatus.OK),
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
