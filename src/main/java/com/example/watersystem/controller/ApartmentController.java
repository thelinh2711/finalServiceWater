package com.example.watersystem.controller;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<List<Apartment>> getAllApartments() {
        return ResponseEntity.ok(apartmentService.getAllApartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apartment> getApartmentById(@PathVariable Long id) {
        return apartmentService.getApartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Apartment>> getApartmentsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(apartmentService.getApartmentsByCustomerId(customerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Apartment>> searchApartmentsByAddress(@RequestParam String address) {
        return ResponseEntity.ok(apartmentService.searchApartmentsByAddress(address));
    }

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<Apartment> createApartment(@PathVariable Long customerId, @RequestBody Apartment apartment) {
        return apartmentService.createApartment(customerId, apartment)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Apartment> updateApartment(@PathVariable Long id, @RequestBody Apartment apartment) {
        return apartmentService.updateApartment(id, apartment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        if (apartmentService.deleteApartment(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/with-active-contracts")
    public ResponseEntity<List<Apartment>> getApartmentsWithActiveContracts() {
        return ResponseEntity.ok(apartmentService.getApartmentsWithActiveContracts());
    }

    @GetMapping("/without-contracts")
    public ResponseEntity<List<Apartment>> getApartmentsWithoutContracts() {
        return ResponseEntity.ok(apartmentService.getApartmentsWithoutContracts());
    }
}