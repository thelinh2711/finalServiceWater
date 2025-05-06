package com.example.watersystem.repository;

import com.example.watersystem.model.AdminUser;
import com.example.watersystem.model.Invoice;
import com.example.watersystem.model.WaterUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByStatus(String status);

    List<Invoice> findByWaterUsage(WaterUsage waterUsage);

    List<Invoice> findByAdminUser(AdminUser adminUser);

    List<Invoice> findByCreatedAtBetween(LocalDate start, LocalDate end);

    List<Invoice> findByTotalAmountGreaterThan(BigDecimal amount);

    @Query("SELECT i FROM Invoice i WHERE i.status = 'CHUA_THANH_TOAN' AND i.createdAt < :date")
    List<Invoice> findOverdueInvoices(LocalDate date);

    @Query("SELECT i.createdAt, SUM(i.totalAmount) FROM Invoice i GROUP BY i.createdAt ORDER BY i.createdAt")
    List<Object[]> findDailyRevenue();

    @Query("SELECT SUBSTRING(i.createdAt, 1, 7), SUM(i.totalAmount) FROM Invoice i GROUP BY SUBSTRING(i.createdAt, 1, 7) ORDER BY SUBSTRING(i.createdAt, 1, 7)")
    List<Object[]> findMonthlyRevenue();
}