package com.example.watersystem.controller;

import com.example.watersystem.model.WaterServiceType;
import com.example.watersystem.service.WaterServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-types")
@RequiredArgsConstructor
public class WaterServiceTypeController {

    private final WaterServiceTypeService waterServiceTypeService;

    @GetMapping
    public ResponseEntity<List<WaterServiceType>> getAllServiceTypes() {
        return ResponseEntity.ok(waterServiceTypeService.getAllServiceTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterServiceType> getServiceTypeById(@PathVariable Long id) {
        return waterServiceTypeService.getServiceTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<WaterServiceType> getServiceTypeByName(@PathVariable String name) {
        return waterServiceTypeService.getServiceTypeByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<WaterServiceType>> searchServiceTypes(@RequestParam String keyword) {
        return ResponseEntity.ok(waterServiceTypeService.searchServiceTypes(keyword));
    }

    @PostMapping
    public ResponseEntity<WaterServiceType> createServiceType(@RequestBody WaterServiceType serviceType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(waterServiceTypeService.createServiceType(serviceType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaterServiceType> updateServiceType(@PathVariable Long id, @RequestBody WaterServiceType serviceType) {
        return waterServiceTypeService.updateServiceType(id, serviceType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Long id) {
        if (waterServiceTypeService.deleteServiceType(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-popularity")
    public ResponseEntity<List<WaterServiceType>> getServiceTypesOrderedByPopularity() {
        return ResponseEntity.ok(waterServiceTypeService.getServiceTypesOrderedByPopularity());
    }

    @GetMapping("/by-max-price")
    public ResponseEntity<List<WaterServiceType>> getServiceTypesWithPriceLowerThan(@RequestParam Integer maxPrice) {
        return ResponseEntity.ok(waterServiceTypeService.getServiceTypesWithPriceLowerThan(maxPrice));
    }
}