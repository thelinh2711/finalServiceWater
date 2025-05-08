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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    // Bước 3, 5, 12 – Lấy tất cả hợp đồng đang hoạt động
    public List<Contract> getAllActiveContracts() {
        return contractRepository.findByActiveTrue();
    }

    public Contract getContractByApartmentId(Integer apartmentId) {
        return contractRepository.findByApartmentId(apartmentId);
    }
    // Bước 48 – Lấy hợp đồng theo ID
    public Contract getById(Integer id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng với ID: " + id));
    }
}