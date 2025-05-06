package com.example.watersystem.controller;

import com.example.watersystem.model.WaterUsage;
import com.example.watersystem.service.WaterUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/water-usages")
@RequiredArgsConstructor
public class WaterUsageController {

    private final WaterUsageService waterUsageService;

    @GetMapping
    public ResponseEntity<List<WaterUsage>> getAllWaterUsages() {
        return ResponseEntity.ok(waterUsageService.getAllWaterUsages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterUsage> getWaterUsageById(@PathVariable Long id) {
        return waterUsageService.getWaterUsageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<WaterUsage>> getWaterUsagesByContractId(@PathVariable Long contractId) {
        return ResponseEntity.ok(waterUsageService.getWaterUsagesByContractId(contractId));
    }

    @GetMapping("/contract/{contractId}/month/{month}")
    public ResponseEntity<List<WaterUsage>> getWaterUsageByContractAndMonth(
            @PathVariable Long contractId, @PathVariable String month) {
        return ResponseEntity.ok(waterUsageService.getWaterUsageByContractAndMonth(contractId, month));
    }

    @PostMapping("/contract/{contractId}")
    public ResponseEntity<WaterUsage> recordWaterUsage(
            @PathVariable Long contractId, @RequestBody WaterUsage waterUsage) {
        return waterUsageService.recordWaterUsage(contractId, waterUsage)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaterUsage> updateWaterUsage(@PathVariable Long id, @RequestBody WaterUsage waterUsage) {
        return waterUsageService.updateWaterUsage(id, waterUsage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterUsage(@PathVariable Long id) {
        if (waterUsageService.deleteWaterUsage(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/high-consumption")
    public ResponseEntity<List<WaterUsage>> getHighConsumptionRecords(
            @RequestParam(defaultValue = "10") Integer threshold) {
        return ResponseEntity.ok(waterUsageService.getHighConsumptionRecords(threshold));
    }

    @GetMapping("/without-invoice")
    public ResponseEntity<List<WaterUsage>> getWaterUsageWithoutInvoice() {
        return ResponseEntity.ok(waterUsageService.getWaterUsageWithoutInvoice());
    }

    @GetMapping("/consumption-by-month")
    public ResponseEntity<List<Object[]>> getTotalConsumptionByMonth() {
        return ResponseEntity.ok(waterUsageService.getTotalConsumptionByMonth());
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<Map<String, Integer>> calculateWaterConsumption(@PathVariable Long id) {
        return waterUsageService.getWaterUsageById(id)
                .map(waterUsage -> {
                    int consumption = waterUsageService.calculateWaterConsumption(waterUsage);
                    return ResponseEntity.ok(Map.of("consumption", consumption));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}