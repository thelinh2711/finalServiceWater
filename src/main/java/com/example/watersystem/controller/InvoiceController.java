package com.example.watersystem.controller;

import com.example.watersystem.model.Invoice;
import com.example.watersystem.model.TieredPrice;
import com.example.watersystem.service.InvoiceService;
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
import java.util.Map;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/detail")
    public String showInvoiceDetail(@RequestParam("invoiceId") Integer invoiceId, Model model) {
        Invoice invoice = invoiceService.getById(invoiceId);
        List<Map<String, Object>> tieredPrices = invoiceService.buildTieredPriceViewList(invoice);
        model.addAttribute("invoice", invoice);
        model.addAttribute("tieredPrices", tieredPrices);
        return "invoiceDetail";
    }
}