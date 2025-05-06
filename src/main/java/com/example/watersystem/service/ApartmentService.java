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

    // Lấy danh sách căn hộ kèm thông tin dịch vụ nước
    public List<Map<String, Object>> getApartmentsWithServiceTypeByCustomerId(int customerId) {
        // Bước 47-51: Lấy danh sách căn hộ từ repository
        List<Apartment> apartments = getApartmentsByCustomerId(customerId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Apartment apartment : apartments) {
            Map<String, Object> apartmentData = new HashMap<>();
            apartmentData.put("apartment", apartment);

            // Bước 52-62: Lấy thông tin hợp đồng và loại dịch vụ
            Contract contract = contractService.getContractByApartmentId(apartment.getId());
            if (contract != null) {
                apartmentData.put("serviceType", contract.getServiceType());
            } else {
                apartmentData.put("serviceType", "Chưa có dịch vụ");
            }

            result.add(apartmentData);
        }

        return result;
    }
}