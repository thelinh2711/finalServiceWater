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
    private final ContractService contractService;

    public WaterUsage getLastUsage(Integer contractId) {
        return waterUsageRepository
                .findTopByContractIdOrderByMonthDesc(contractId)
                .orElse(null);
    }

    public WaterUsage createUsage(Integer contractId, int currentIndex, String month) {
        WaterUsage last = getLastUsage(contractId);
        int previous = (last != null) ? last.getCurrentIndex() : 0;

        Contract contract = contractService.getById(contractId);
        int used = currentIndex - previous;

        WaterUsage usage = new WaterUsage();
        usage.setContract(contract);
        usage.setPreviousIndex(previous);
        usage.setCurrentIndex(currentIndex);
        usage.setUsedIndex(used);
        usage.setMonth(month);

        return waterUsageRepository.save(usage);
    }
}