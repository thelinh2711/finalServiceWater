package com.example.watersystem.repository;

import com.example.watersystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByFullNameContaining(String fullName);

    List<Customer> findByBirthDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT c FROM Customer c WHERE c.email LIKE %:domain%")
    List<Customer> findByEmailDomain(String domain);

    @Query("SELECT c FROM Customer c JOIN c.contracts cont GROUP BY c HAVING COUNT(cont) > :minContracts")
    List<Customer> findCustomersWithMultipleContracts(int minContracts);
}