package com.example.watersystem.service;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Contract;
import com.example.watersystem.model.Customer;
import com.example.watersystem.model.WaterServiceType;
import com.example.watersystem.repository.ApartmentRepository;
import com.example.watersystem.repository.ContractRepository;
import com.example.watersystem.repository.CustomerRepository;
import com.example.watersystem.repository.WaterServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final CustomerRepository customerRepository;
    private final ApartmentRepository apartmentRepository;
    private final WaterServiceTypeRepository waterServiceTypeRepository;

    @Transactional(readOnly = true)
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Contract> getContractsByCustomerId(Long customerId) {
        return customerRepository.findById(customerId)
                .map(contractRepository::findByCustomer)
                .orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<Contract> getContractsByApartmentId(Long apartmentId) {
        return apartmentRepository.findById(apartmentId)
                .map(contractRepository::findByApartment)
                .orElse(List.of());
    }

    @Transactional
    public Optional<Contract> createContract(Contract contract, Long customerId, Long apartmentId, Long serviceTypeId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<Apartment> apartmentOpt = apartmentRepository.findById(apartmentId);
        Optional<WaterServiceType> serviceTypeOpt = waterServiceTypeRepository.findById(serviceTypeId);

        if (customerOpt.isPresent() && apartmentOpt.isPresent() && serviceTypeOpt.isPresent()) {
            contract.setCustomer(customerOpt.get());
            contract.setApartment(apartmentOpt.get());
            contract.setServiceType(serviceTypeOpt.get());
            contract.setSignedDate(LocalDate.now());
            return Optional.of(contractRepository.save(contract));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<Contract> updateContract(Long id, Contract contractDetails) {
        return contractRepository.findById(id)
                .map(contract -> {
                    contract.setSignedDate(contractDetails.getSignedDate());

                    if (contractDetails.getCustomer() != null && contractDetails.getCustomer().getId() != null) {
                        customerRepository.findById(contractDetails.getCustomer().getId())
                                .ifPresent(contract::setCustomer);
                    }

                    if (contractDetails.getApartment() != null && contractDetails.getApartment().getId() != null) {
                        apartmentRepository.findById(contractDetails.getApartment().getId())
                                .ifPresent(contract::setApartment);
                    }

                    if (contractDetails.getServiceType() != null && contractDetails.getServiceType().getId() != null) {
                        waterServiceTypeRepository.findById(contractDetails.getServiceType().getId())
                                .ifPresent(contract::setServiceType);
                    }

                    return contractRepository.save(contract);
                });
    }

    @Transactional
    public boolean deleteContract(Long id) {
        return contractRepository.findById(id)
                .map(contract -> {
                    contractRepository.delete(contract);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<Contract> getContractsBySignedDateRange(LocalDate startDate, LocalDate endDate) {
        return contractRepository.findBySignedDateBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Contract> getContractsWithoutWaterUsage(LocalDate beforeDate) {
        return contractRepository.findContractsWithoutWaterUsage(beforeDate);
    }

    @Transactional(readOnly = true)
    public Optional<Contract> findActiveContractForApartmentAndServiceType(Long apartmentId, Long serviceTypeId) {
        Optional<Apartment> apartmentOpt = apartmentRepository.findById(apartmentId);
        Optional<WaterServiceType> serviceTypeOpt = waterServiceTypeRepository.findById(serviceTypeId);

        if (apartmentOpt.isPresent() && serviceTypeOpt.isPresent()) {
            return contractRepository.findByApartmentAndServiceTypeAndSignedDateLessThanEqual(
                    apartmentOpt.get(), serviceTypeOpt.get(), LocalDate.now());
        }

        return Optional.empty();
    }
}