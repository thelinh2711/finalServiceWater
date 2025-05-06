package com.example.watersystem.service;

import com.example.watersystem.model.*;
import com.example.watersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final WaterUsageRepository waterUsageRepository;
    private final AdminUserRepository adminUserRepository;
    private final TieredPriceRepository tieredPriceRepository;

    @Transactional(readOnly = true)
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status);
    }

    @Transactional
    public Optional<Invoice> generateInvoice(Long waterUsageId, Long adminUserId) {
        Optional<WaterUsage> waterUsageOpt = waterUsageRepository.findById(waterUsageId);
        Optional<AdminUser> adminUserOpt = adminUserRepository.findById(adminUserId);

        if (waterUsageOpt.isPresent() && adminUserOpt.isPresent()) {
            WaterUsage waterUsage = waterUsageOpt.get();
            AdminUser adminUser = adminUserOpt.get();

            // Kiểm tra xem đã có hóa đơn cho bản ghi sử dụng nước này chưa
            if (waterUsage.getInvoice() != null) {
                return Optional.empty();
            }

            // Tính toán lượng nước sử dụng
            int consumption = waterUsage.getCurrentIndex() - waterUsage.getPreviousIndex();

            // Tìm bậc giá phù hợp với lượng nước sử dụng
            Optional<TieredPrice> tieredPriceOpt = tieredPriceRepository.findApplicableTierForUsage(
                    consumption, waterUsage.getContract().getServiceType().getId());

            if (tieredPriceOpt.isPresent()) {
                TieredPrice tieredPrice = tieredPriceOpt.get();

                // Tính tổng số tiền
                BigDecimal totalAmount = BigDecimal.valueOf(consumption * tieredPrice.getPricePerM3());

                // Tạo hóa đơn mới
                Invoice invoice = Invoice.builder()
                        .waterUsage(waterUsage)
                        .totalAmount(totalAmount)
                        .status("CHUA_THANH_TOAN")
                        .createdAt(LocalDate.now())
                        .adminUser(adminUser)
                        .build();

                return Optional.of(invoiceRepository.save(invoice));
            }
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<Invoice> updateInvoiceStatus(Long id, String status) {
        return invoiceRepository.findById(id)
                .map(invoice -> {
                    invoice.setStatus(status);
                    return invoiceRepository.save(invoice);
                });
    }

    @Transactional
    public boolean deleteInvoice(Long id) {
        return invoiceRepository.findById(id)
                .map(invoice -> {
                    invoiceRepository.delete(invoice);
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<Invoice> getOverdueInvoices(LocalDate date) {
        return invoiceRepository.findOverdueInvoices(date);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDailyRevenue() {
        return invoiceRepository.findDailyRevenue();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getMonthlyRevenue() {
        return invoiceRepository.findMonthlyRevenue();
    }
}