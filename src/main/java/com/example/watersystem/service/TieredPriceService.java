package com.example.watersystem.service;

import com.example.watersystem.model.TieredPrice;
import com.example.watersystem.model.WaterServiceType;
import com.example.watersystem.repository.TieredPriceRepository;
import com.example.watersystem.repository.WaterServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TieredPriceService {

    private final TieredPriceRepository tieredPriceRepository;
    private final WaterServiceTypeRepository waterServiceTypeRepository;

    @Transactional(readOnly = true)
    public List<TieredPrice> getAllTieredPrices() {
        return tieredPriceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TieredPrice> getTieredPriceById(Long id) {
        return tieredPriceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<TieredPrice> getTieredPricesByServiceTypeId(Long serviceTypeId) {
        return waterServiceTypeRepository.findById(serviceTypeId)
                .map(tieredPriceRepository::findByServiceType)
                .orElse(Collections.emptyList());
    }

    @Transactional
    public Optional<TieredPrice> createTieredPrice(Long serviceTypeId, TieredPrice tieredPrice) {
        return waterServiceTypeRepository.findById(serviceTypeId)
                .map(serviceType -> {
                    tieredPrice.setServiceType(serviceType);
                    return tieredPriceRepository.save(tieredPrice);
                });
    }

    @Transactional
    public Optional<TieredPrice> updateTieredPrice(Long id, TieredPrice tieredPriceDetails) {
        return tieredPriceRepository.findById(id)
                .map(tieredPrice -> {
                    tieredPrice.setMinValue(tieredPriceDetails.getMinValue());
                    tieredPrice.setMaxValue(tieredPriceDetails.getMaxValue());
                    tieredPrice.setPricePerM3(tieredPriceDetails.getPricePerM3());

                    if (tieredPriceDetails.getServiceType() != null && tieredPriceDetails.getServiceType().getId() != null) {
                        waterServiceTypeRepository.findById(tieredPriceDetails.getServiceType().getId())
                                .ifPresent(tieredPrice::setServiceType);
                    }

                    return tieredPriceRepository.save(tieredPrice);
                });
    }

    @Transactional
    public boolean deleteTieredPrice(Long id) {
        return tieredPriceRepository.findById(id)
                .map(tieredPrice -> {
                    tieredPriceRepository.delete(tieredPrice);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<TieredPrice> findApplicableTierForUsage(Integer usage, Long serviceTypeId) {
        return tieredPriceRepository.findApplicableTierForUsage(usage, serviceTypeId);
    }

    @Transactional(readOnly = true)
    public List<TieredPrice> getTiersByServiceTypeOrdered(Long serviceTypeId) {
        return tieredPriceRepository.findTiersByServiceTypeOrdered(serviceTypeId);
    }
}