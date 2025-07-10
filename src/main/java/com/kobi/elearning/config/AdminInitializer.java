package com.kobi.elearning.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kobi.elearning.constant.PredefinedRole;
import com.kobi.elearning.entity.Role;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.repository.RoleRepository;
import com.kobi.elearning.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		// Tạo các role với description rõ ràng nếu chưa có
		if (!roleRepository.existsByName(PredefinedRole.ADMIN)) {
			Role role = new Role();
			role.setName(PredefinedRole.ADMIN);
			role.setDescription("Quản trị hệ thống");
			roleRepository.save(role);
		}
		if (!roleRepository.existsByName(PredefinedRole.TEACHER)) {
			Role role = new Role();
			role.setName(PredefinedRole.TEACHER);
			role.setDescription("Giáo viên");
			roleRepository.save(role);
		}
		if (!roleRepository.existsByName(PredefinedRole.STUDENT)) {
			Role role = new Role();
			role.setName(PredefinedRole.STUDENT);
			role.setDescription("Học viên");
			roleRepository.save(role);
		}
		if (!roleRepository.existsByName(PredefinedRole.MENTOR)) {
			Role role = new Role();
			role.setName(PredefinedRole.MENTOR);
			role.setDescription("Cố vấn");
			roleRepository.save(role);
		}

		// Tạo tài khoản admin nếu chưa có
		String adminUsername = "admin";
		if (!userRepository.existsByUserName(adminUsername)) {
			User admin = new User();
			admin.setUserName(adminUsername);
			admin.setPassWord(passwordEncoder.encode("admin123"));
			admin.setFullName("Administrator");
			Role adminRole = roleRepository.findByName(PredefinedRole.ADMIN);
			admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
			userRepository.save(admin);
		}
	}
}
