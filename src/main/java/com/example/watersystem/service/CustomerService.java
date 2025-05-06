package com.example.watersystem.service;

import com.example.watersystem.model.Customer;
import com.example.watersystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByFullNameContaining(name);
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(LocalDate.now());
        return customerRepository.save(customer);
    }

    @Transactional
    public Optional<Customer> updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFullName(customerDetails.getFullName());
                    customer.setPhone(customerDetails.getPhone());
                    customer.setEmail(customerDetails.getEmail());
                    customer.setBirthDate(customerDetails.getBirthDate());
                    return customerRepository.save(customer);
                });
    }

    @Transactional
    public boolean deleteCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithMultipleContracts(int minContracts) {
        return customerRepository.findCustomersWithMultipleContracts(minContracts);
    }
}