package com.example.watersystem.repository;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
    @Query("SELECT a FROM Apartment a WHERE a.customer.id = :customerId")
    List<Apartment> findByCustomerId(@Param("customerId") int customerId);

}