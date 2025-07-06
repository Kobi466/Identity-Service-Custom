package com.kobi.elearning.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kobi.elearning.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<Object> findByUserName(String userName);

	boolean existsByUserName(String userName);
}
