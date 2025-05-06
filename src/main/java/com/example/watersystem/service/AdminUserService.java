package com.example.watersystem.service;

import com.example.watersystem.model.AdminUser;
import com.example.watersystem.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserService(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Xác thực người dùng admin
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return true nếu xác thực thành công, false nếu thất bại
     */
    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {
        Optional<AdminUser> adminUserOpt = adminUserRepository.findByUsername(username);

        if (adminUserOpt.isEmpty()) {
            return false;
        }

        AdminUser adminUser = adminUserOpt.get();

        // So sánh mật khẩu
        return passwordEncoder.matches(password, adminUser.getPassword());
    }

    /**
     * Lấy thông tin admin theo tên đăng nhập
     * @param username Tên đăng nhập
     * @return Optional chứa thông tin admin nếu tìm thấy
     */
    @Transactional(readOnly = true)
    public Optional<AdminUser> getAdminUserByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }
    @Transactional(readOnly = true)
    public List<AdminUser> getAllAdminUsers() {
        return adminUserRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AdminUser> getAdminUserById(Integer id) {
        return adminUserRepository.findById(id);
    }

    @Transactional
    public AdminUser createAdminUser(AdminUser adminUser) {
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        return adminUserRepository.save(adminUser);
    }


}