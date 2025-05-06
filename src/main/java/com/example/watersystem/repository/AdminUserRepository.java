package com.example.watersystem.repository;

import com.example.watersystem.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByUsername(String username);

    Optional<AdminUser> findByEmail(String email);

    List<AdminUser> findByRole(String role);

    @Query("SELECT a FROM AdminUser a JOIN a.invoices i GROUP BY a ORDER BY COUNT(i) DESC")
    List<AdminUser> findAdminUsersOrderedByInvoiceCount();

    @Query("SELECT a FROM AdminUser a JOIN a.payments p GROUP BY a ORDER BY SUM(p.amount) DESC")
    List<AdminUser> findAdminUsersOrderedByPaymentAmount();
}