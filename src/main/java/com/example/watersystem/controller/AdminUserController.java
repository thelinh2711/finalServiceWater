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

    @GetMapping("/users")
    public String getAllAdminUsers(Model model) {
        List<AdminUser> adminUsers = adminUserService.getAllAdminUsers();
        model.addAttribute("adminUsers", adminUsers);
        return "users"; // Nếu bạn có trang users.html trong thư mục templates
    }

    @GetMapping("/users/{id}")
    public String getAdminUserById(@PathVariable Integer id, Model model) {
        Optional<AdminUser> adminUser = adminUserService.getAdminUserById(id);
        if (adminUser.isPresent()) {
            model.addAttribute("adminUser", adminUser.get());
            return "user-details"; // Nếu bạn có trang user-details.html
        } else {
            model.addAttribute("error", "Không tìm thấy người dùng");
            return "error"; // Nếu bạn có trang error.html
        }
    }

    @GetMapping("/users/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("adminUser", new AdminUser());
        return "user-form"; // Nếu bạn có trang user-form.html
    }

    @PostMapping("/users/save")
    public String saveAdminUser(@ModelAttribute AdminUser adminUser, Model model) {
        try {
            adminUserService.createAdminUser(adminUser);
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("adminUser", adminUser);
            return "user-form"; // Nếu bạn có trang user-form.html
        }
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model) {
        Optional<AdminUser> adminUser = adminUserService.getAdminUserById(id);
        if (adminUser.isPresent()) {
            model.addAttribute("adminUser", adminUser.get());
            return "user-form"; // Nếu bạn có trang user-form.html
        } else {
            model.addAttribute("error", "Không tìm thấy người dùng");
            return "error"; // Nếu bạn có trang error.html
        }
    }

}