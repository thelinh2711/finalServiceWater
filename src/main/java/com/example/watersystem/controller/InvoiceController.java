package com.example.watersystem.controller;

import com.example.watersystem.model.Invoice;
import com.example.watersystem.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Invoice>> getInvoicesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(invoiceService.getInvoicesByStatus(status));
    }

//    @GetMapping("/date-range")
//    public ResponseEntity<List<Invoice>> getInvoicesByDateRange(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        return ResponseEntity.ok(invoiceService.getInvoicesByDateRange(startDate, endDate));
//    }

    @PostMapping("/generate")
    public ResponseEntity<Invoice> generateInvoice(
            @RequestParam Long waterUsageId, @RequestParam Long adminUserId) {
        return invoiceService.generateInvoice(waterUsageId, adminUserId)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Invoice> updateInvoiceStatus(@PathVariable Long id, @RequestParam String status) {
        return invoiceService.updateInvoiceStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        if (invoiceService.deleteInvoice(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Invoice>> getOverdueInvoices(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate overdueDate = date != null ? date : LocalDate.now().minusDays(30);
        return ResponseEntity.ok(invoiceService.getOverdueInvoices(overdueDate));
    }

    @GetMapping("/daily-revenue")
    public ResponseEntity<List<Object[]>> getDailyRevenue() {
        return ResponseEntity.ok(invoiceService.getDailyRevenue());
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<Object[]>> getMonthlyRevenue() {
        return ResponseEntity.ok(invoiceService.getMonthlyRevenue());
    }
}