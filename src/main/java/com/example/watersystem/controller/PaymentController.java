package com.example.watersystem.controller;

import com.example.watersystem.model.Payment;
import com.example.watersystem.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<Payment>> getPaymentsByInvoiceId(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(paymentService.getPaymentsByInvoiceId(invoiceId));
    }

    @GetMapping("/admin-user/{adminUserId}")
    public ResponseEntity<List<Payment>> getPaymentsByAdminUserId(@PathVariable Long adminUserId) {
        return ResponseEntity.ok(paymentService.getPaymentsByAdminUserId(adminUserId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(paymentService.getPaymentsByDateRange(startDate, endDate));
    }

    @PostMapping("/record")
    public ResponseEntity<Payment> recordPayment(
            @RequestParam Long invoiceId,
            @RequestParam Long adminUserId,
            @RequestParam BigDecimal amount) {
        return paymentService.recordPayment(invoiceId, adminUserId, amount)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (paymentService.deletePayment(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/invoice/{invoiceId}/total")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPaymentsByInvoice(@PathVariable Long invoiceId) {
        BigDecimal total = paymentService.getTotalPaymentsByInvoice(invoiceId);
        return ResponseEntity.ok(Map.of("totalPaid", total));
    }

    @GetMapping("/daily")
    public ResponseEntity<List<Object[]>> getDailyPayments() {
        return ResponseEntity.ok(paymentService.getDailyPayments());
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<Object[]>> getMonthlyPayments() {
        return ResponseEntity.ok(paymentService.getMonthlyPayments());
    }

    @GetMapping("/above-average")
    public ResponseEntity<List<Payment>> getPaymentsAboveAverage() {
        return ResponseEntity.ok(paymentService.getPaymentsAboveAverage());
    }

    @GetMapping("/amount-greater-than")
    public ResponseEntity<List<Payment>> getPaymentsWithAmountGreaterThan(@RequestParam BigDecimal amount) {
        return ResponseEntity.ok(paymentService.getPaymentsWithAmountGreaterThan(amount));
    }
}