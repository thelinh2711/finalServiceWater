package com.example.watersystem.controller;

import com.example.watersystem.model.TieredPrice;
import com.example.watersystem.service.TieredPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tiered-prices")
@RequiredArgsConstructor
public class TieredPriceController {

    private final TieredPriceService tieredPriceService;

    @GetMapping
    public ResponseEntity<List<TieredPrice>> getAllTieredPrices() {
        return ResponseEntity.ok(tieredPriceService.getAllTieredPrices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TieredPrice> getTieredPriceById(@PathVariable Long id) {
        return tieredPriceService.getTieredPriceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/service-type/{serviceTypeId}")
    public ResponseEntity<List<TieredPrice>> getTieredPricesByServiceTypeId(@PathVariable Long serviceTypeId) {
        return ResponseEntity.ok(tieredPriceService.getTieredPricesByServiceTypeId(serviceTypeId));
    }

    @PostMapping("/service-type/{serviceTypeId}")
    public ResponseEntity<TieredPrice> createTieredPrice(@PathVariable Long serviceTypeId, @RequestBody TieredPrice tieredPrice) {
        return tieredPriceService.createTieredPrice(serviceTypeId, tieredPrice)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TieredPrice> updateTieredPrice(@PathVariable Long id, @RequestBody TieredPrice tieredPrice) {
        return tieredPriceService.updateTieredPrice(id, tieredPrice)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTieredPrice(@PathVariable Long id) {
        if (tieredPriceService.deleteTieredPrice(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/applicable")
    public ResponseEntity<TieredPrice> findApplicableTierForUsage(
            @RequestParam Integer usage, @RequestParam Long serviceTypeId) {
        return tieredPriceService.findApplicableTierForUsage(usage, serviceTypeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/service-type/{serviceTypeId}/ordered")
    public ResponseEntity<List<TieredPrice>> getTiersByServiceTypeOrdered(@PathVariable Long serviceTypeId) {
        return ResponseEntity.ok(tieredPriceService.getTiersByServiceTypeOrdered(serviceTypeId));
    }
}