package com.example.watersystem.service;

import com.example.watersystem.model.AdminUser;
import com.example.watersystem.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<AdminUser> getAllAdminUsers() {
        return adminUserRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AdminUser> getAdminUserById(Long id) {
        return adminUserRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<AdminUser> getAdminUserByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    @Transactional
    public AdminUser createAdminUser(AdminUser adminUser) {
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        return adminUserRepository.save(adminUser);
    }

    @Transactional
    public Optional<AdminUser> updateAdminUser(Long id, AdminUser adminUserDetails) {
        return adminUserRepository.findById(id)
                .map(adminUser -> {
                    adminUser.setUsername(adminUserDetails.getUsername());
                    adminUser.setFullName(adminUserDetails.getFullName());
                    adminUser.setEmail(adminUserDetails.getEmail());
                    adminUser.setRole(adminUserDetails.getRole());

                    // Chỉ cập nhật mật khẩu nếu được cung cấp
                    if (adminUserDetails.getPassword() != null && !adminUserDetails.getPassword().isEmpty()) {
                        adminUser.setPassword(passwordEncoder.encode(adminUserDetails.getPassword()));
                    }

                    return adminUserRepository.save(adminUser);
                });
    }

    @Transactional
    public boolean deleteAdminUser(Long id) {
        return adminUserRepository.findById(id)
                .map(adminUser -> {
                    adminUserRepository.delete(adminUser);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<AdminUser> getAdminUsersByRole(String role) {
        return adminUserRepository.findByRole(role);
    }

    @Transactional(readOnly = true)
    public List<AdminUser> getAdminUsersOrderedByInvoiceCount() {
        return adminUserRepository.findAdminUsersOrderedByInvoiceCount();
    }

    @Transactional(readOnly = true)
    public List<AdminUser> getAdminUsersOrderedByPaymentAmount() {
        return adminUserRepository.findAdminUsersOrderedByPaymentAmount();
    }
}