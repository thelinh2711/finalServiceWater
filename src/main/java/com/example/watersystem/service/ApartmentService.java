package com.example.watersystem.service;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Customer;
import com.example.watersystem.repository.ApartmentRepository;
import com.example.watersystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Apartment> getApartmentById(Long id) {
        return apartmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Apartment> getApartmentsByCustomerId(Long customerId) {
        return customerRepository.findById(customerId)
                .map(apartmentRepository::findByCustomer)
                .orElse(Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<Apartment> searchApartmentsByAddress(String address) {
        return apartmentRepository.findByAddressContaining(address);
    }

    @Transactional
    public Optional<Apartment> createApartment(Long customerId, Apartment apartment) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    apartment.setCustomer(customer);
                    return apartmentRepository.save(apartment);
                });
    }

    @Transactional
    public Optional<Apartment> updateApartment(Long id, Apartment apartmentDetails) {
        return apartmentRepository.findById(id)
                .map(apartment -> {
                    apartment.setAddress(apartmentDetails.getAddress());
                    if (apartmentDetails.getCustomer() != null && apartmentDetails.getCustomer().getId() != null) {
                        customerRepository.findById(apartmentDetails.getCustomer().getId())
                                .ifPresent(apartment::setCustomer);
                    }
                    return apartmentRepository.save(apartment);
                });
    }

    @Transactional
    public boolean deleteApartment(Long id) {
        return apartmentRepository.findById(id)
                .map(apartment -> {
                    apartmentRepository.delete(apartment);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<Apartment> getApartmentsWithActiveContracts() {
        return apartmentRepository.findApartmentsWithActiveContracts();
    }

    @Transactional(readOnly = true)
    public List<Apartment> getApartmentsWithoutContracts() {
        return apartmentRepository.findApartmentsWithoutContracts();
    }
}