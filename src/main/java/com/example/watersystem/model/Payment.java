package com.example.watersystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tblPayment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", precision = 19, scale = 0)
    private BigDecimal amount;

    @Column(name = "paidDate")
    private LocalDate paidDate;

    @ManyToOne
    @JoinColumn(name = "tblAdminUserId")
    private AdminUser adminUser;

    @ManyToOne
    @JoinColumn(name = "tblInvoiceId")
    private Invoice invoice;
}