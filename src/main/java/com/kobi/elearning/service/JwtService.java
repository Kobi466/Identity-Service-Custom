package com.kobi.elearning.service;

import org.springframework.stereotype.Service;

import com.kobi.elearning.entity.User;

@Service
public interface JwtService {
	String generateAccessToken(User user);

	String generateRefreshToken(User user);

	boolean validateToken(String token);

	String getIdFromToken(String token);
}
