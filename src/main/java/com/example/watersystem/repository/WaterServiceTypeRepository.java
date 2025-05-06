package com.example.watersystem.repository;

import com.example.watersystem.model.WaterServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterServiceTypeRepository extends JpaRepository<WaterServiceType, Long> {

    Optional<WaterServiceType> findByName(String name);

    List<WaterServiceType> findByNameContaining(String keyword);

    @Query("SELECT wst FROM WaterServiceType wst JOIN wst.contracts c GROUP BY wst ORDER BY COUNT(c) DESC")
    List<WaterServiceType> findServiceTypesOrderedByPopularity();

    @Query("SELECT wst FROM WaterServiceType wst JOIN wst.tieredPrices tp WHERE tp.pricePerM3 <= :maxPrice")
    List<WaterServiceType> findServiceTypesWithPriceLowerThan(Integer maxPrice);
}