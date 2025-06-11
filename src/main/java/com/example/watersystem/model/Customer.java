package com.example.watersystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tblCustomer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("fullName")
    @Column(name = "fullName", length = 255)
    private String fullName;

    @JsonProperty("phone")
    @Column(length = 255)
    private String phone;

    @JsonProperty("email")
    @Column(length = 255)
    private String email;

    @JsonProperty("birthDate")
    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "creatAt", updatable = false)
    private LocalDate createdAt;

}
