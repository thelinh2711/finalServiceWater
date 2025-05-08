package com.example.watersystem.controller;

import org.springframework.ui.Model;
import com.example.watersystem.model.AdminUser;
import com.example.watersystem.service.AdminUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Trỏ đến templates/login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        // Gọi service để xác thực
        if (adminUserService.authenticate(username, password)) {
            // Lấy thông tin admin
            Optional<AdminUser> adminUserOpt = adminUserService.getAdminUserByUsername(username);

            if (adminUserOpt.isPresent()) {
                // Lưu thông tin vào session
                session.setAttribute("adminUser", adminUserOpt.get());

                // Chuyển hướng đến trang chủ
                return "redirect:/home";
            }
        }

        // Đăng nhập thất bại, quay lại trang đăng nhập với thông báo lỗi
        model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
        return "login";
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        // Kiểm tra xem đã đăng nhập chưa
        AdminUser adminUser = (AdminUser) session.getAttribute("adminUser");

        if (adminUser == null) {
            // Chưa đăng nhập, chuyển hướng về trang đăng nhập
            return "redirect:/login";
        }

        // Đã đăng nhập, truyền thông tin admin vào model
        model.addAttribute("admin", adminUser);

        return "home"; // Trỏ đến templates/home.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa thông tin phiên
        session.invalidate();

        // Chuyển hướng về trang đăng nhập
        return "redirect:/login";
    }

}