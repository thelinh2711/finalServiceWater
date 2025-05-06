package com.example.watersystem.service;

import com.example.watersystem.model.AdminUser;
import com.example.watersystem.model.Invoice;
import com.example.watersystem.model.Payment;
import com.example.watersystem.repository.AdminUserRepository;
import com.example.watersystem.repository.InvoiceRepository;
import com.example.watersystem.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {


}