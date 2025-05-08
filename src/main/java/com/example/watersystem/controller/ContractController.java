package com.example.watersystem.controller;

import com.example.watersystem.model.Contract;
import com.example.watersystem.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping("/contracts")
    public String getAllActiveContracts(Model model) {
        List<Contract> contracts = contractService.getAllActiveContracts();
        model.addAttribute("contracts", contracts);
        return "apartmentList";
    }
}