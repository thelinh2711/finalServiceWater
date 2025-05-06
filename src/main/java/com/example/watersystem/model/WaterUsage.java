package com.example.watersystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tblWaterUsage")
public class WaterUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "month", length = 7)
    private String month;

    @Column(name = "previousIndex")
    private Integer previousIndex;

    @Column(name = "currentIndex")
    private Integer currentIndex;

    @ManyToOne
    @JoinColumn(name = "tblContractId")
    private Contract contract;

    @OneToOne(mappedBy = "waterUsage")
    private Invoice invoice;
}