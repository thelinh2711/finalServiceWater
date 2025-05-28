package com.example.watersystem.service;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Contract;
import com.example.watersystem.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;

    // ✅ Lấy tất cả hợp đồng đang hoạt động
    public List<Contract> getAllActiveContracts() {
        return contractRepository.findByActiveTrue();
    }

//    public Contract getContractByApartmentId(Integer apartmentId) {
//        return contractRepository.findByApartmentId(apartmentId);
//    }

    // ✅ Lấy hợp đồng theo đối tượng Apartment (thay vì ID)
    public Contract getByApartment(Apartment apartment) {
        return contractRepository.findByApartment(apartment);
    }

    // ✅ Lấy hợp đồng theo ID
    public Contract getById(Integer id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng với ID: " + id));
    }
}
