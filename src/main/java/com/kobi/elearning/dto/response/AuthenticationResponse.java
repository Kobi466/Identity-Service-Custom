package com.kobi.elearning.dto.response;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
	String accessToken;
	String refreshToken;
	Instant expiresAt;
	UserResponse user;
}
