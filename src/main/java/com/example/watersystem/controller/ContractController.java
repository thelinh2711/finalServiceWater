package com.example.watersystem.controller;

import com.example.watersystem.model.Contract;
import com.example.watersystem.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        return contractService.getContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Contract>> getContractsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(contractService.getContractsByCustomerId(customerId));
    }

    @GetMapping("/apartment/{apartmentId}")
    public ResponseEntity<List<Contract>> getContractsByApartmentId(@PathVariable Long apartmentId) {
        return ResponseEntity.ok(contractService.getContractsByApartmentId(apartmentId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Contract>> getContractsBySignedDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(contractService.getContractsBySignedDateRange(startDate, endDate));
    }

    @PostMapping
    public ResponseEntity<Contract> createContract(
            @RequestBody Contract contract,
            @RequestParam Long customerId,
            @RequestParam Long apartmentId,
            @RequestParam Long serviceTypeId) {
        return contractService.createContract(contract, customerId, apartmentId, serviceTypeId)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        return contractService.updateContract(id, contract)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        if (contractService.deleteContract(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/without-water-usage")
    public ResponseEntity<List<Contract>> getContractsWithoutWaterUsage(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate) {
        LocalDate date = beforeDate != null ? beforeDate : LocalDate.now();
        return ResponseEntity.ok(contractService.getContractsWithoutWaterUsage(date));
    }

    @GetMapping("/active")
    public ResponseEntity<Contract> findActiveContractForApartmentAndServiceType(
            @RequestParam Long apartmentId,
            @RequestParam Long serviceTypeId) {
        return contractService.findActiveContractForApartmentAndServiceType(apartmentId, serviceTypeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}