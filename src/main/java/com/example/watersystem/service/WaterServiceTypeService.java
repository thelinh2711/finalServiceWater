package com.example.watersystem.service;

import com.example.watersystem.model.WaterServiceType;
import com.example.watersystem.repository.WaterServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaterServiceTypeService {

    private final WaterServiceTypeRepository waterServiceTypeRepository;

    @Transactional(readOnly = true)
    public List<WaterServiceType> getAllServiceTypes() {
        return waterServiceTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<WaterServiceType> getServiceTypeById(Long id) {
        return waterServiceTypeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<WaterServiceType> getServiceTypeByName(String name) {
        return waterServiceTypeRepository.findByName(name);
    }

    @Transactional
    public WaterServiceType createServiceType(WaterServiceType serviceType) {
        return waterServiceTypeRepository.save(serviceType);
    }

    @Transactional
    public Optional<WaterServiceType> updateServiceType(Long id, WaterServiceType serviceTypeDetails) {
        return waterServiceTypeRepository.findById(id)
                .map(serviceType -> {
                    serviceType.setName(serviceTypeDetails.getName());
                    serviceType.setNote(serviceTypeDetails.getNote());
                    return waterServiceTypeRepository.save(serviceType);
                });
    }

    @Transactional
    public boolean deleteServiceType(Long id) {
        return waterServiceTypeRepository.findById(id)
                .map(serviceType -> {
                    waterServiceTypeRepository.delete(serviceType);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<WaterServiceType> searchServiceTypes(String keyword) {
        return waterServiceTypeRepository.findByNameContaining(keyword);
    }

    @Transactional(readOnly = true)
    public List<WaterServiceType> getServiceTypesOrderedByPopularity() {
        return waterServiceTypeRepository.findServiceTypesOrderedByPopularity();
    }

    @Transactional(readOnly = true)
    public List<WaterServiceType> getServiceTypesWithPriceLowerThan(Integer maxPrice) {
        return waterServiceTypeRepository.findServiceTypesWithPriceLowerThan(maxPrice);
    }
}