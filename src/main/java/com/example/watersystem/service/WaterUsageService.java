package com.example.watersystem.service;

import com.example.watersystem.model.Contract;
import com.example.watersystem.model.WaterUsage;
import com.example.watersystem.repository.ContractRepository;
import com.example.watersystem.repository.WaterUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaterUsageService {

    private final WaterUsageRepository waterUsageRepository;
    private final ContractRepository contractRepository;

    @Transactional(readOnly = true)
    public List<WaterUsage> getAllWaterUsages() {
        return waterUsageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<WaterUsage> getWaterUsageById(Long id) {
        return waterUsageRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<WaterUsage> getWaterUsagesByContractId(Long contractId) {
        return contractRepository.findById(contractId)
                .map(waterUsageRepository::findByContract)
                .orElse(List.of());
    }

    @Transactional
    public Optional<WaterUsage> recordWaterUsage(Long contractId, WaterUsage waterUsage) {
        return contractRepository.findById(contractId)
                .map(contract -> {
                    // Set current month if not provided
                    if (waterUsage.getMonth() == null || waterUsage.getMonth().isEmpty()) {
                        waterUsage.setMonth(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
                    }

                    // Get last water usage record to set as previous index
                    Optional<WaterUsage> lastRecord = waterUsageRepository
                            .findByContractAndMonthOrderByIdDesc(contract, waterUsage.getMonth());

                    if (lastRecord.isPresent()) {
                        waterUsage.setPreviousIndex(lastRecord.get().getCurrentIndex());
                    } else {
                        // If no previous record, previous index should be 0 or a default value
                        if (waterUsage.getPreviousIndex() == null) {
                            waterUsage.setPreviousIndex(0);
                        }
                    }

                    waterUsage.setContract(contract);
                    return waterUsageRepository.save(waterUsage);
                });
    }

    @Transactional
    public Optional<WaterUsage> updateWaterUsage(Long id, WaterUsage waterUsageDetails) {
        return waterUsageRepository.findById(id)
                .map(waterUsage -> {
                    waterUsage.setMonth(waterUsageDetails.getMonth());
                    waterUsage.setPreviousIndex(waterUsageDetails.getPreviousIndex());
                    waterUsage.setCurrentIndex(waterUsageDetails.getCurrentIndex());

                    if (waterUsageDetails.getContract() != null && waterUsageDetails.getContract().getId() != null) {
                        contractRepository.findById(waterUsageDetails.getContract().getId())
                                .ifPresent(waterUsage::setContract);
                    }

                    return waterUsageRepository.save(waterUsage);
                });
    }

    @Transactional
    public boolean deleteWaterUsage(Long id) {
        return waterUsageRepository.findById(id)
                .map(waterUsage -> {
                    waterUsageRepository.delete(waterUsage);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<WaterUsage> getWaterUsageByContractAndMonth(Long contractId, String month) {
        return contractRepository.findById(contractId)
                .map(contract -> waterUsageRepository.findByContractAndMonth(contract, month))
                .orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<WaterUsage> getHighConsumptionRecords(Integer threshold) {
        return waterUsageRepository.findHighConsumptionRecords(threshold);
    }

    @Transactional(readOnly = true)
    public List<WaterUsage> getWaterUsageWithoutInvoice() {
        return waterUsageRepository.findWaterUsageWithoutInvoice();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getTotalConsumptionByMonth() {
        return waterUsageRepository.findTotalConsumptionByMonth();
    }

    @Transactional(readOnly = true)
    public int calculateWaterConsumption(WaterUsage waterUsage) {
        return waterUsage.getCurrentIndex() - waterUsage.getPreviousIndex();
    }
}