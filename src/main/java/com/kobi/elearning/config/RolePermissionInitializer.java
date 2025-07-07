package com.kobi.elearning.config;

import com.kobi.elearning.entity.Permission;
import com.kobi.elearning.entity.Role;
import com.kobi.elearning.repository.PermissionRepository;
import com.kobi.elearning.repository.RoleRepository;
import com.kobi.elearning.constant.PredefinedRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RolePermissionInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        // Lấy các permission mẫu (nên kiểm tra null và log rõ ràng)
        Permission userView = permissionRepository.findByName(("USER_VIEW"));
        Permission userEdit = permissionRepository.findByName("USER_EDIT");
        Permission courseView = permissionRepository.findByName("COURSE_VIEW");
        Permission courseEdit = permissionRepository.findByName("COURSE_EDIT");

        // Gán permission cho STUDENT
        Role student = roleRepository.findByName(PredefinedRole.STUDENT);
        if (student != null) {
            Set<Permission> studentPerms = new HashSet<>();
            if (userView != null) studentPerms.add(userView);
            if (courseView != null) studentPerms.add(courseView);
            student.setPermissions(studentPerms);
            roleRepository.save(student);
        }

        // Gán permission cho TEACHER
        Role teacher = roleRepository.findByName(PredefinedRole.TEACHER);
        if (teacher != null) {
            Set<Permission> teacherPerms = new HashSet<>();
            if (userView != null) teacherPerms.add(userView);
            if (courseView != null) teacherPerms.add(courseView);
            if (courseEdit != null) teacherPerms.add(courseEdit);
            teacher.setPermissions(teacherPerms);
            roleRepository.save(teacher);
        }

        // Gán permission cho ADMIN (nên gán tất cả quyền hiện có)
        Role admin = roleRepository.findByName(PredefinedRole.ADMIN);
        if (admin != null) {
            Set<Permission> allPerms = new HashSet<>(permissionRepository.findAll());
            admin.setPermissions(allPerms);
            roleRepository.save(admin);
        }

        // Gán permission cho MENTOR
        Role mentor = roleRepository.findByName(PredefinedRole.MENTOR);
        if (mentor != null) {
            Set<Permission> mentorPerms = new HashSet<>();
            if (courseView != null) mentorPerms.add(courseView);
            mentor.setPermissions(mentorPerms);
            roleRepository.save(mentor);
        }
    }
}
