package com.example.watersystem.service;

import com.example.watersystem.model.Customer;
import com.example.watersystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Lấy danh sách tất cả khách hàng
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Lấy khách hàng theo ID (dành cho controller)
    public Customer getById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
    }

    // ✅ Cập nhật thông tin khách hàng theo object (hướng đối tượng)
    public Customer update(Customer origin, Customer updated) {
        origin.setFullName(updated.getFullName());
        origin.setEmail(updated.getEmail());
        origin.setPhone(updated.getPhone());
        origin.setBirthDate(updated.getBirthDate());
        return customerRepository.save(origin);
    }


    // Lưu mới hoặc cập nhật khách hàng
    public void save(Customer customer) {
        customerRepository.save(customer);
    }
}
