package com.kobi.elearning.config;

import com.kobi.elearning.entity.Permission;
import com.kobi.elearning.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PermissionInitializer implements CommandLineRunner {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        createPermissionIfNotExists("USER_VIEW", "Xem thông tin người dùng");
        createPermissionIfNotExists("USER_EDIT", "Chỉnh sửa người dùng");
        createPermissionIfNotExists("COURSE_VIEW", "Xem khoá học");
        createPermissionIfNotExists("COURSE_EDIT", "Chỉnh sửa khoá học");
    }

    private void createPermissionIfNotExists(String name, String description) {
        if (!permissionRepository.existsByName((name))) {
            Permission permission = new Permission();
            permission.setName(name);
            permission.setDescription(description);
            permissionRepository.save(permission);
        }
    }
}

