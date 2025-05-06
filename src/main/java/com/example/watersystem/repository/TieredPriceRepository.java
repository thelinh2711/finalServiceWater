package com.example.watersystem.repository;

import com.example.watersystem.model.TieredPrice;
import com.example.watersystem.model.WaterServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TieredPriceRepository extends JpaRepository<TieredPrice, Long> {

    List<TieredPrice> findByServiceType(WaterServiceType serviceType);

    List<TieredPrice> findByPricePerM3LessThanEqual(Integer maxPrice);

    @Query("SELECT tp FROM TieredPrice tp WHERE :usage BETWEEN tp.minValue AND tp.maxValue AND tp.serviceType.id = :serviceTypeId")
    Optional<TieredPrice> findApplicableTierForUsage(Integer usage, Long serviceTypeId);

    @Query("SELECT tp FROM TieredPrice tp WHERE tp.serviceType.id = :serviceTypeId ORDER BY tp.minValue ASC")
    List<TieredPrice> findTiersByServiceTypeOrdered(Long serviceTypeId);
}