package com.example.watersystem.controller;

import com.example.watersystem.dto.CustomerRevenueDTO;
import com.example.watersystem.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticController {

    private final InvoiceService invoiceService;

    @GetMapping("/revenue-by-customer")
    public String showRevenuePage(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            Model model) {

        if (from != null && to != null) {
            List<CustomerRevenueDTO> revenueList = invoiceService.getCustomerRevenue(from, to);
            model.addAttribute("revenues", revenueList);
        }

        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "revenueByCustomer";
    }
}
