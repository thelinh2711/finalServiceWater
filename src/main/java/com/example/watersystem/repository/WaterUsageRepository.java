package com.example.watersystem.repository;

import com.example.watersystem.model.Contract;
import com.example.watersystem.model.WaterUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterUsageRepository extends JpaRepository<WaterUsage, Integer> {
    Optional<WaterUsage> findTopByContractIdOrderByMonthDesc(Integer contractId);
    Optional<WaterUsage> findByContractIdAndMonth(Integer contractId, String month);

    @Query("SELECT DISTINCT wu.month FROM WaterUsage wu WHERE wu.contract = :contract")
    List<String> findMonthsByContract(@Param("contract") Contract contract);

}