package com.example.watersystem.repository;

import com.example.watersystem.model.WaterServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterServiceTypeRepository extends JpaRepository<WaterServiceType, Integer> {

}