package com.example.watersystem.controller;

import com.example.watersystem.model.Apartment;
import com.example.watersystem.model.Contract;
import com.example.watersystem.model.Customer;
import com.example.watersystem.service.ApartmentService;
import com.example.watersystem.service.ContractService;
import com.example.watersystem.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private ContractService contractService;

    // Bước 17-18: Quản trị viên chọn "Quản lý khách hàng" và gọi CustomerController
    @GetMapping("/customers")
    public String getAllCustomers(Model model, HttpSession session) {
        // Kiểm tra đăng nhập
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        // Bước 19-27: Lấy danh sách khách hàng
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);

        // Bước 28-29: Trả về giao diện customerList.html
        return "customerList";
    }

    // Bước 30-32: Quản trị viên chọn một khách hàng, customerList.html gọi CustomerController
    @GetMapping("/customers/{id}")
    public String getCustomerById(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        // Lấy thông tin chi tiết khách hàng
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);

        return "customerDetail";
    }

    // Bước 66-67: Quản trị viên chọn sửa thông tin khách hàng
    @GetMapping("/customers/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        // Kiểm tra đăng nhập
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        // Lấy thông tin khách hàng hiện tại
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);

        // Hiển thị form sửa
        return "editCustomer";
    }

    // Bước 68-69: Quản trị viên sửa thông tin và nhấn "Lưu thay đổi"
    @PostMapping("/customers/update/{id}")
    public String updateCustomer(@PathVariable int id,
                                 @ModelAttribute Customer customer,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        // Kiểm tra đăng nhập
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        // Bước 70-81: Cập nhật thông tin khách hàng
        customerService.updateCustomer(id, customer);

        // Thêm thông báo thành công
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin khách hàng thành công");

        // Bước 82: Chuyển về trang chi tiết
        return "redirect:/customers/" + id;
    }
}