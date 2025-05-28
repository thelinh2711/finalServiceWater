package com.example.watersystem.controller;

import com.example.watersystem.model.Customer;
import com.example.watersystem.service.ApartmentService;
import com.example.watersystem.service.ContractService;
import com.example.watersystem.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final ApartmentService apartmentService;
    private final ContractService contractService;

    @GetMapping("/customers")
    public String getAllCustomers(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customerList";
    }

    @GetMapping("/customers/{id}")
    public String getCustomerById(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        Customer customer = customerService.getById(id);
        model.addAttribute("customer", customer);
        return "customerDetail";
    }

    @GetMapping("/customers/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        Customer customer = customerService.getById(id);
        model.addAttribute("customer", customer);
        return "editCustomer";
    }

    @PostMapping("/customers/update")
    public String updateCustomer(@ModelAttribute("customer") Customer updatedCustomer,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        Customer origin = customerService.getById(updatedCustomer.getId());
        customerService.update(origin, updatedCustomer);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin khách hàng thành công");
        return "redirect:/customerDetail/" + updatedCustomer.getId();
    }

    @GetMapping("/customers/add")
    public String showAddCustomerForm(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        model.addAttribute("customer", new Customer());
        return "addCustomer";
    }

    @PostMapping("/customers/add")
    public String addCustomer(@ModelAttribute Customer customer,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/login";
        }

        customerService.save(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm khách hàng mới thành công");
        return "redirect:/customers";
    }
}
