package com.example.watersystem.service;

import com.example.watersystem.dto.CustomerRevenueDTO;
import com.example.watersystem.model.*;
import com.example.watersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final WaterUsageRepository waterUsageRepository;
    private final TieredPriceRepository tieredPriceRepository;
    private final ContractService contractService;

    public List<CustomerRevenueDTO> getCustomerRevenue(LocalDate from, LocalDate to) {
        return invoiceRepository.getCustomerRevenueBetween(from, to);
    }

    public Invoice getById(Integer id) {
        return invoiceRepository.findById(id)
                  .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
    public List<TieredPrice> getTieredPricesForInvoice(Invoice invoice) {
        WaterServiceType serviceType = invoice.getWaterUsage().getContract().getServiceType();
        return tieredPriceRepository.findByServiceType(serviceType);
    }

    public Invoice createInvoiceFromUsage(WaterUsage usage) {
        Contract contract = usage.getContract();
        String month = usage.getMonth();

        // Kiểm tra đã tồn tại usage cùng tháng cho hợp đồng này
        Optional<WaterUsage> existing = waterUsageRepository
                .findByContractIdAndMonth(contract.getId(), month);

        if (existing.isPresent()) {
            throw new RuntimeException("Tháng này đã được tạo hóa đơn.");
        }

        // Tìm chỉ số trước đó (nếu có)
        WaterUsage last = waterUsageRepository
                .findTopByContractIdOrderByMonthDesc(contract.getId())
                .orElse(null);

        int previous = (last != null) ? last.getCurrentIndex() : 0;
        int current = usage.getCurrentIndex();
        int used = current - previous;

        usage.setPreviousIndex(previous);
        usage.setUsedIndex(used);

        // Tính tiền nước theo bậc giá
        List<TieredPrice> tierList = tieredPriceRepository.findByServiceTypeId(
                contract.getServiceType().getId()
        );
        BigDecimal totalAmount = applyTieredPricing(usage, tierList);

        // Tạo hóa đơn gắn với usage
        Invoice invoice = Invoice.builder()
                .waterUsage(usage)
                .totalAmount(totalAmount)
                .status("CREATED")
                .createdAt(LocalDate.now())
                .build();

        return invoiceRepository.save(invoice);
    }


    private BigDecimal applyTieredPricing(WaterUsage usage, List<TieredPrice> tiers) {
        BigDecimal total = BigDecimal.ZERO;
        int remainingUsage = usage.getCurrentIndex() - (usage.getPreviousIndex() != null ? usage.getPreviousIndex() : 0);

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
    public List<Map<String, Object>> buildTieredPriceViewList(Invoice invoice) {
        List<TieredPrice> tiers = getTieredPricesForInvoice(invoice);
        int used = invoice.getWaterUsage().getUsedIndex();
        int remainingUsage = used;

        List<Map<String, Object>> result = new ArrayList<>();

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
                    if(remainingUsage<=max-min+1){
                        tierUsage = remainingUsage;
                    } else {
                        tierUsage = max - min + 1;
                    }
                }
            }
            if (tierUsage > 0) {
                Map<String, Object> row = new HashMap<>();
                String range = (maxObj == null)
                        ? min + "+ m³"
                        : min + " - " + maxObj + " m³";
                int subtotal = tierUsage * pricePerM3;

                row.put("range", range);
                row.put("unitPrice", pricePerM3);
                row.put("subtotal", subtotal);

                result.add(row);
                remainingUsage -= tierUsage;
            }

            if (remainingUsage <= 0) break;
        }

        return result;
    }
}
