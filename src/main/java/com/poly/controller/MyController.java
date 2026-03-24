package com.poly.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    // Ai cũng vào được (không cần đăng nhập)
    @GetMapping({"/", "/poly/url0"})
    public String method0(Model model) {
        model.addAttribute("message", "/poly/url0 => Cho phép tất cả (Khách vãng lai)");
        return "page"; 
    }

    // Chỉ cần Đã đăng nhập (Bất kể role gì: USER, ADMIN hay OAUTH)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/poly/url1")
    public String method1(Model model) {
        model.addAttribute("message", "/poly/url1 => Đã đăng nhập mới được xem");
        return "page";
    }

    // Chỉ Role USER mới được vào
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/poly/url2")
    public String method2(Model model) {
        model.addAttribute("message", "/poly/url2 => Dành cho USER");
        return "page";
    }

    // Chỉ Role ADMIN mới được vào
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/poly/url3")
    public String method3(Model model) {
        model.addAttribute("message", "/poly/url3 => Dành cho ADMIN");
        return "page";
    }

    // Có Role USER HOẶC ADMIN đều được vào
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/poly/url4")
    public String method4(Model model) {
        model.addAttribute("message", "/poly/url4 => Dành cho cả USER và ADMIN");
        return "page";
    }
}
