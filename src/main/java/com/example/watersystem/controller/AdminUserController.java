package com.example.watersystem.controller;

import com.example.watersystem.model.AdminUser;
import com.example.watersystem.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<AdminUser>> getAllAdminUsers() {
        return ResponseEntity.ok(adminUserService.getAllAdminUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> getAdminUserById(@PathVariable Long id) {
        return adminUserService.getAdminUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AdminUser> getAdminUserByUsername(@PathVariable String username) {
        return adminUserService.getAdminUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<AdminUser>> getAdminUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(adminUserService.getAdminUsersByRole(role));
    }

    @PostMapping
    public ResponseEntity<AdminUser> createAdminUser(@RequestBody AdminUser adminUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUserService.createAdminUser(adminUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUser> updateAdminUser(@PathVariable Long id, @RequestBody AdminUser adminUser) {
        return adminUserService.updateAdminUser(id, adminUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable Long id) {
        if (adminUserService.deleteAdminUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-invoice-count")
    public ResponseEntity<List<AdminUser>> getAdminUsersOrderedByInvoiceCount() {
        return ResponseEntity.ok(adminUserService.getAdminUsersOrderedByInvoiceCount());
    }

    @GetMapping("/by-payment-amount")
    public ResponseEntity<List<AdminUser>> getAdminUsersOrderedByPaymentAmount() {
        return ResponseEntity.ok(adminUserService.getAdminUsersOrderedByPaymentAmount());
    }
}