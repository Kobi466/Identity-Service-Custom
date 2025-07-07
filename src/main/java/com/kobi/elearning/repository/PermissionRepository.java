package com.kobi.elearning.repository;

import com.kobi.elearning.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByName(String name);

    Permission findByName(String name);
}
