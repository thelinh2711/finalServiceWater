package com.example.watersystem.controller;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Contract;
import com.example.watersystem.model.Customer;
import com.example.watersystem.service.ApartmentService;
import com.example.watersystem.service.ContractService;
import com.example.watersystem.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CustomerService customerService;

    //Bước 43-44: customerDetail.html tiếp tục gọi lớp ApartmentController
    @GetMapping("/customerDetail/{customerId}")
    public String getCustomerDetail(@PathVariable int customerId, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        // Gọi dữ liệu từ CustomerController
        Customer customer = customerService.getCustomerById(customerId);
        model.addAttribute("customer", customer);

        // Lấy danh sách căn hộ
        List<Apartment> apartments = apartmentService.getApartmentsByCustomerId(customerId);
        List<Map<String, Object>> apartmentsWithService = new ArrayList<>();

        for (Apartment apartment : apartments) {
            Map<String, Object> apartmentData = new HashMap<>();
            apartmentData.put("apartment", apartment);

            Contract contract = contractService.getContractByApartmentId(apartment.getId());
            if (contract != null && contract.getServiceType() != null) {
                apartmentData.put("serviceType", contract.getServiceType().getName());
                apartmentData.put("note", contract.getServiceType().getNote());
            } else {
                apartmentData.put("serviceType", "Chưa có dịch vụ");
                apartmentData.put("note", "");
            }

            apartmentsWithService.add(apartmentData);
        }

        model.addAttribute("apartments", apartmentsWithService);

        return "customerDetail";
    }
}