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

    List<WaterUsage> findByContract(Contract contract);

    List<WaterUsage> findByContractAndMonth(Contract contract, String month);

    Optional<WaterUsage> findByContractAndMonthOrderByIdDesc(Contract contract, String month);

    @Query("SELECT wu FROM WaterUsage wu WHERE wu.currentIndex - wu.previousIndex > :threshold")
    List<WaterUsage> findHighConsumptionRecords(Integer threshold);

    @Query("SELECT wu FROM WaterUsage wu WHERE wu.invoice IS NULL")
    List<WaterUsage> findWaterUsageWithoutInvoice();

    @Query("SELECT wu.month, SUM(wu.currentIndex - wu.previousIndex) FROM WaterUsage wu GROUP BY wu.month ORDER BY wu.month")
    List<Object[]> findTotalConsumptionByMonth();
}