package com.example.watersystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tblInvoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "totalAmount", precision = 19, scale = 0)
    private BigDecimal totalAmount;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "createdAt")
    private LocalDate createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tblWaterUsageId")
    private WaterUsage waterUsage;

    @ManyToOne
    @JoinColumn(name = "tblAdminUserId")
    private AdminUser adminUser;

}