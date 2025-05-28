package com.example.watersystem.controller;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Contract;
import com.example.watersystem.model.Customer;
import com.example.watersystem.service.ApartmentService;
import com.example.watersystem.service.ContractService;
import com.example.watersystem.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final ContractService contractService;
    private final CustomerService customerService;

    @GetMapping("/customerDetail/{customerId}")
    public String getCustomerDetail(@PathVariable int customerId, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        // ✅ Truy xuất object Customer
        Customer customer = customerService.getById(customerId);
        model.addAttribute("customer", customer);

        // ✅ Truy xuất danh sách căn hộ của khách hàng
        List<Apartment> apartments = apartmentService.getApartmentsByCustomer(customer);
        List<Map<String, Object>> apartmentsWithService = new ArrayList<>();

        for (Apartment apartment : apartments) {
            Map<String, Object> apartmentData = new HashMap<>();
            apartmentData.put("apartment", apartment);

            // ✅ Truy xuất object Contract bằng object Apartment
            Contract contract = contractService.getByApartment(apartment);

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
