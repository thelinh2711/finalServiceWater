package com.example.watersystem.repository;

import com.example.watersystem.dto.CustomerRevenueDTO;
import com.example.watersystem.model.AdminUser;
import com.example.watersystem.model.Invoice;
import com.example.watersystem.model.WaterUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query(value = """
        SELECT 
            c.id AS customerId,
            c.full_name AS customerName,
            SUM(i.total_amount) AS totalRevenue
        FROM tbl_invoice i
        JOIN tbl_water_usage wu ON i.tbl_water_usage_id = wu.id
        JOIN tbl_contract ct ON wu.tbl_contract_id = ct.id
        JOIN tbl_customer c ON ct.tbl_customer_id = c.id
        WHERE i.status = 'CREATED'
          AND i.created_at BETWEEN :startDate AND :endDate
        GROUP BY c.id, c.full_name
        ORDER BY totalRevenue DESC
    """, nativeQuery = true)
    List<CustomerRevenueDTO> getCustomerRevenueBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}