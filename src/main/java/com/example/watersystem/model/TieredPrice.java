package com.example.watersystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tblTieredPrice")
public class TieredPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "minValue")
    private Integer minValue;

    @Column(name = "maxValue")
    private Integer maxValue;

    @Column(name = "pricePerM3")
    private Integer pricePerM3;

    @ManyToOne
    @JoinColumn(name = "tblWaterServiceTypeId")
    private WaterServiceType serviceType;
}