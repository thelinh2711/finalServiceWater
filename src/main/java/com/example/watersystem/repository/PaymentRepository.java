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
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByInvoice(Invoice invoice);

    List<Payment> findByAdminUser(AdminUser adminUser);

    List<Payment> findByPaidDateBetween(LocalDate start, LocalDate end);

    List<Payment> findByAmountGreaterThan(BigDecimal amount);

    @Query("SELECT FUNCTION('DATE_FORMAT', p.paidDate, '%Y-%m-%d') as day, SUM(p.amount) FROM Payment p GROUP BY day ORDER BY day")
    List<Object[]> findDailyPayments();

    @Query("SELECT FUNCTION('DATE_FORMAT', p.paidDate, '%Y-%m') as month, SUM(p.amount) FROM Payment p GROUP BY month ORDER BY month")
    List<Object[]> findMonthlyPayments();

    @Query("SELECT p FROM Payment p WHERE p.amount > (SELECT AVG(p2.amount) FROM Payment p2)")
    List<Payment> findPaymentsAboveAverage();
}