package com.example.watersystem.service;

import com.example.watersystem.model.*;
import com.example.watersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final WaterUsageRepository waterUsageRepository;
    private final TieredPriceRepository tieredPriceRepository;

    public Invoice getById(Integer id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
    public List<TieredPrice> getTieredPricesForInvoice(Invoice invoice) {
        WaterServiceType serviceType = invoice.getWaterUsage().getContract().getServiceType();
        return tieredPriceRepository.findByServiceType(serviceType);
    }

    public Invoice generateInvoice(int waterUsageId) {
        WaterUsage usage = waterUsageRepository.findById(waterUsageId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chỉ số nước"));

        int usedIndex = usage.getUsedIndex();
        int serviceTypeId = usage.getContract().getServiceType().getId();

        List<TieredPrice> tierList = tieredPriceRepository.findByServiceTypeId(serviceTypeId);
        BigDecimal totalAmount = applyTieredPricing(usedIndex, tierList);

        Invoice invoice = Invoice.builder()
                .waterUsage(usage)
                .totalAmount(totalAmount)
                .status("CREATED")
                .createdAt(LocalDate.now())
                .build();

        return invoiceRepository.save(invoice);
    }

    private BigDecimal applyTieredPricing(int used, List<TieredPrice> tiers) {
        BigDecimal total = BigDecimal.ZERO;
        int remainingUsage = used;

        for (TieredPrice tier : tiers) {
            int min = tier.getMinValue();
            Integer maxObj = tier.getMaxValue();
            int pricePerM3 = tier.getPricePerM3();

            int tierUsage;

            if (maxObj == null) {
                // Bậc cuối: không giới hạn
                tierUsage = remainingUsage;
            } else {
                int max = maxObj;
                // Tính đúng lượng nước thuộc bậc này
                if (min == 0) {
                    tierUsage = Math.min(remainingUsage, max);
                } else {
                    tierUsage = Math.min(remainingUsage, max - min + 1);
                }
            }

            if (tierUsage > 0) {
                BigDecimal tierCost = BigDecimal.valueOf(tierUsage)
                        .multiply(BigDecimal.valueOf(pricePerM3));
                total = total.add(tierCost);
                remainingUsage -= tierUsage;
            }

            if (remainingUsage <= 0) break;
        }

        return total;
    }
}
