package com.example.watersystem.controller;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping("/customer/{customerId}")
    public String getApartmentsByCustomerId(@PathVariable int customerId, Model model) {
        List<Map<String, Object>> apartmentsWithServiceType =
                apartmentService.getApartmentsWithServiceTypeByCustomerId(customerId);
        model.addAttribute("apartments", apartmentsWithServiceType);
        return "customerDetail";
    }
}