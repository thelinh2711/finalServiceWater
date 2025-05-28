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

    public WaterUsage getLastUsage(Contract contract) {
        return waterUsageRepository
                .findTopByContractIdOrderByMonthDesc(contract.getId())
                .orElse(null);
    }
    public List<String> getUsedMonths(Contract contract) {
        return waterUsageRepository.findMonthsByContract(contract);
    }

}