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

    List<Apartment> findByCustomer(Customer customer);

    List<Apartment> findByAddressContaining(String addressKeyword);

    @Query("SELECT a FROM Apartment a JOIN a.contracts c GROUP BY a HAVING COUNT(c) > 0")
    List<Apartment> findApartmentsWithActiveContracts();

    @Query("SELECT a FROM Apartment a LEFT JOIN a.contracts c WHERE c IS NULL")
    List<Apartment> findApartmentsWithoutContracts();
}