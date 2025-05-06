package com.example.watersystem.service;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Contract;
import com.example.watersystem.model.Customer;
import com.example.watersystem.repository.ApartmentRepository;
import com.example.watersystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ContractService contractService;

    public List<Apartment> getApartmentsByCustomerId(int customerId) {
        return apartmentRepository.findByCustomerId(customerId);
    }

    public List<Map<String, Object>> getApartmentsWithServiceTypeByCustomerId(int customerId) {
        List<Apartment> apartments = getApartmentsByCustomerId(customerId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Apartment apartment : apartments) {
            Map<String, Object> apartmentData = new HashMap<>();
            apartmentData.put("apartment", apartment);

            Contract contract = contractService.getContractByApartmentId(apartment.getId());
            if (contract != null) {
                apartmentData.put("serviceType", contract.getServiceType());
            }

            result.add(apartmentData);
        }

        return result;
    }
}