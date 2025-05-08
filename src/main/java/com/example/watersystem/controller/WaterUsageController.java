package com.example.watersystem.controller;

import com.example.watersystem.model.Contract;
import com.example.watersystem.model.Invoice;
import com.example.watersystem.model.WaterUsage;
import com.example.watersystem.service.ContractService;
import com.example.watersystem.service.InvoiceService;
import com.example.watersystem.service.WaterUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/water-usage")
public class WaterUsageController {

    private final ContractService contractService;
    private final WaterUsageService waterUsageService;
    private final InvoiceService invoiceService;

    // Bước 15–27: Hiển thị form nhập chỉ số
    @GetMapping("/input")
    public String showInputForm(@RequestParam("apartmentId") Integer apartmentId, Model model) {
        Contract contract = contractService.getContractByApartmentId(apartmentId);
        WaterUsage lastUsage = waterUsageService.getLastUsage(contract.getId());

        model.addAttribute("contractId", contract.getId());
        model.addAttribute("previousIndex", (lastUsage != null) ? lastUsage.getCurrentIndex() : 0);

        // 👇 Bổ sung 3 dòng này để truyền dữ liệu cho giao diện
        model.addAttribute("address", contract.getApartment().getAddress());
        model.addAttribute("owner", contract.getCustomer().getFullName());
        model.addAttribute("service", contract.getServiceType().getName());

        return "waterUsageInput";
    }


    // Bước 41–63: Nhận form, lưu chỉ số mới, tạo hóa đơn, chuyển sang trang hóa đơn
    @PostMapping("/create")
    public String createUsage(@RequestParam("contractId") Integer contractId,
                              @RequestParam("currentIndex") int currentIndex,
                              @RequestParam("month") String month,
                              RedirectAttributes redirectAttributes) {

        WaterUsage newUsage = waterUsageService.createUsage(contractId, currentIndex, month);
        Invoice invoice = invoiceService.generateInvoice(newUsage.getId());

        redirectAttributes.addAttribute("invoiceId", invoice.getId());
        return "redirect:/invoices/detail?invoiceId=" + invoice.getId();
    }
}