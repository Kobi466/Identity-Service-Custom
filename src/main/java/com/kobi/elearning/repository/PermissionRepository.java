package com.kobi.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kobi.elearning.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {
	boolean existsByName(String name);

	Permission findByName(String name);
}
