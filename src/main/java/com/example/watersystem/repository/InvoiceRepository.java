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
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

}