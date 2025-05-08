package com.example.watersystem.repository;

import com.example.watersystem.model.Contract;
import com.example.watersystem.model.WaterUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterUsageRepository extends JpaRepository<WaterUsage, Integer> {
    Optional<WaterUsage> findTopByContractIdOrderByMonthDesc(Integer contractId);
}