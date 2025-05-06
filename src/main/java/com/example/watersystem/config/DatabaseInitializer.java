package com.example.watersystem.config;

import com.example.watersystem.model.AdminUser;
import com.example.watersystem.repository.AdminUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseInitializer(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initializeDatabase() {
        // Kiểm tra xem đã có admin chưa
        if (adminUserRepository.findByUsername("admin").isEmpty()) {
            // Tạo tài khoản admin mặc định
            AdminUser adminUser = new AdminUser();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("password")); // Mật khẩu "password"
            adminUser.setFullName("Admin System");
            adminUser.setEmail("admin@example.com");
            adminUser.setRole("ADMIN");

            // Lưu vào database
            adminUserRepository.save(adminUser);

            System.out.println("Created default admin user: admin/password");
        }
    }
}