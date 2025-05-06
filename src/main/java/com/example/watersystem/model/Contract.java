package com.example.watersystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tblContract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "signedDate")
    private LocalDate signedDate;

    @ManyToOne
    @JoinColumn(name = "tblApartmentId")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "tblCustomerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "tblWaterServiceTypeId")
    private WaterServiceType serviceType;

    @OneToMany(mappedBy = "contract")
    private List<WaterUsage> waterUsages;
}