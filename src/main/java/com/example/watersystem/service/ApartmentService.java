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

    // Bước 46: ApartmentService thực hiện phương thức getApartmentsByCustomerId(customerId)
    public List<Apartment> getApartmentsByCustomerId(int customerId) {
        // Bước 47-51: Gọi repository để lấy danh sách căn hộ
        return apartmentRepository.findByCustomerId(customerId);
    }

}