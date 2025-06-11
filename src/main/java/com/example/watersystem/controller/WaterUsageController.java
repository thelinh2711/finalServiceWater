package com.example.watersystem.controller;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Contract;
import com.example.watersystem.model.Invoice;
import com.example.watersystem.model.WaterUsage;
import com.example.watersystem.service.ApartmentService;
import com.example.watersystem.service.ContractService;
import com.example.watersystem.service.InvoiceService;
import com.example.watersystem.service.WaterUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/water-usage")
public class WaterUsageController {

    private final ContractService contractService;
    private final WaterUsageService waterUsageService;
    private final InvoiceService invoiceService;
    private final ApartmentService apartmentService;

    @GetMapping("/input")
    public String showInputForm(@RequestParam("apartmentId") Integer apartmentId, Model model) {
        Apartment apartment = apartmentService.getById(apartmentId);
        Contract contract = contractService.getByApartment(apartment);
        WaterUsage lastUsage = waterUsageService.getLastUsage(contract);

        // Truy danh sách các tháng đã có hóa đơn
        List<String> usedMonths = waterUsageService.getUsedMonths(contract); // kiểu yyyy-MM

        model.addAttribute("contract", contract);
        model.addAttribute("previousIndex", (lastUsage != null) ? lastUsage.getCurrentIndex() : 0);
        model.addAttribute("usedMonths", usedMonths);

        return "waterUsageInput";
    }

    // Xử lý tạo chỉ số và hóa đơn nước
    @PostMapping("/create")
    public String createUsageAndInvoice(@RequestParam("contractId") Integer contractId,
                                        @RequestParam("currentIndex") int currentIndex,
                                        @RequestParam("month") String month,
                                        RedirectAttributes redirectAttributes) {
        try {
            // Lấy đối tượng Contract
            Contract contract = contractService.getById(contractId);

            // Tạo WaterUsage với thông tin đầu vào
            WaterUsage usage = new WaterUsage();
            usage.setContract(contract);
            usage.setCurrentIndex(currentIndex);
            usage.setMonth(month);

            // Gọi service tạo hóa đơn từ WaterUsage (hướng đối tượng)
            Invoice invoice = invoiceService.createInvoiceFromUsage(usage);

            // Chuyển hướng đến trang chi tiết hóa đơn
            return "redirect:/invoices/detail?invoiceId=" + invoice.getId();

        } catch (RuntimeException e) {
            // Đưa thông báo lỗi ra giao diện
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            // Truy xuất lại đối tượng Contract để lấy apartmentId
            Contract contract = contractService.getById(contractId);
            redirectAttributes.addAttribute("apartmentId", contract.getApartment().getId());

            //Quay lại form nhập
            return "redirect:/water-usage/input";
        }
    }
}
