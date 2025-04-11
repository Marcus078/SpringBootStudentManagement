package com.example.StudentManagement.controller;


import com.example.StudentManagement.Services.AdminService;
import com.example.StudentManagement.model.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AdminService adminService;

    // Constructor injection
    @Autowired
    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }


    // Admin Registration Endpoint
    @PostMapping("/register")
    public Admin register(@RequestBody Admin admin) {
        return adminService.registerUser(admin);
    }

    // Admin Login Endpoint
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return adminService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    // Static class to capture login request
    public static class LoginRequest {
        private String email;
        private String password;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

