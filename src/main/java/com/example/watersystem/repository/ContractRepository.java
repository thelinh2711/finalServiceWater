package com.example.watersystem.repository;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Contract;
import com.example.watersystem.model.Customer;
import com.example.watersystem.model.WaterServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    Contract findByApartmentId(int apartmentId);

    List<Contract> findByCustomer(Customer customer);

    List<Contract> findByApartment(Apartment apartment);

    List<Contract> findByServiceType(WaterServiceType serviceType);

    List<Contract> findBySignedDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT c FROM Contract c WHERE c.signedDate <= :date AND (SELECT COUNT(wu) FROM WaterUsage wu WHERE wu.contract = c) = 0")
    List<Contract> findContractsWithoutWaterUsage(LocalDate date);

    @Query("SELECT c FROM Contract c JOIN c.waterUsages wu GROUP BY c ORDER BY COUNT(wu) DESC")
    List<Contract> findContractsOrderedByWaterUsageCount();

    Optional<Contract> findByApartmentAndServiceTypeAndSignedDateLessThanEqual(Apartment apartment, WaterServiceType serviceType, LocalDate currentDate);
}