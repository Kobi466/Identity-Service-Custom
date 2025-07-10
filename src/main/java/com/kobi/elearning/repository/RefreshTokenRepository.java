package com.kobi.elearning.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kobi.elearning.entity.RefreshToken;
import com.kobi.elearning.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByUser(Optional<User> user);

	RefreshToken findByToken(String token);

	List<RefreshToken> findAllByUser(User user);
}
