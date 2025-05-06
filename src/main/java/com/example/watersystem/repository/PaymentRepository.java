package com.example.watersystem.repository;

import com.example.watersystem.model.AdminUser;
import com.example.watersystem.model.Invoice;
import com.example.watersystem.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoice(Invoice invoice);

    List<Payment> findByAdminUser(AdminUser adminUser);

    List<Payment> findByPaidDateBetween(LocalDate start, LocalDate end);

    List<Payment> findByAmountGreaterThan(BigDecimal amount);

    @Query("SELECT p.paidDate, SUM(p.amount) FROM Payment p GROUP BY p.paidDate ORDER BY p.paidDate")
    List<Object[]> findDailyPayments();

    @Query("SELECT SUBSTRING(p.paidDate, 1, 7), SUM(p.amount) FROM Payment p GROUP BY SUBSTRING(p.paidDate, 1, 7) ORDER BY SUBSTRING(p.paidDate, 1, 7)")
    List<Object[]> findMonthlyPayments();

    @Query("SELECT p FROM Payment p WHERE p.amount > (SELECT AVG(p2.amount) FROM Payment p2)")
    List<Payment> findPaymentsAboveAverage();
}