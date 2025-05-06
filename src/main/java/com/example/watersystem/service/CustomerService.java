package com.example.watersystem.service;

import com.example.watersystem.model.Customer;
import com.example.watersystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Bước 20: Lớp CustomerService thực hiện phương thức getAllCustomers()
    public List<Customer> getAllCustomers() {
        // Bước 21-27: Gọi repository để lấy danh sách khách hàng
        return customerRepository.findAll();
    }

    // Bước 34: Lớp CustomerService thực hiện phương thức getCustomerById(id)
    public Customer getCustomerById(int id) {
        // Bước 35-41: Gọi repository để lấy chi tiết khách hàng theo ID
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
    }

    // Bước 72: CustomerService thực hiện phương thức updateCustomer(id, customer)
    public Customer updateCustomer(int id, Customer updatedCustomer) {
        // Bước 73-75: Gọi repository để tìm bản ghi cũ
        Customer existingCustomer = getCustomerById(id);

        // Bước 76: Cập nhật các trường thông tin
        existingCustomer.setFullName(updatedCustomer.getFullName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setBirthDate(updatedCustomer.getBirthDate());

        // Bước 77-79: Lưu thay đổi vào DB
        return customerRepository.save(existingCustomer);
    }
}