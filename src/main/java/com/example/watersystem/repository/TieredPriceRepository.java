package com.example.watersystem.repository;

import com.example.watersystem.model.TieredPrice;
import com.example.watersystem.model.WaterServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TieredPriceRepository extends JpaRepository<TieredPrice, Integer> {
    List<TieredPrice> findByServiceType(WaterServiceType serviceType);
    List<TieredPrice> findByServiceTypeId(Integer serviceTypeId);
}