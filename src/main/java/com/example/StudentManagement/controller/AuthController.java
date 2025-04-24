package com.example.StudentManagement.controller;

import com.example.StudentManagement.Services.AdminService;
import com.example.StudentManagement.model.Admin;
import com.example.StudentManagement.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    // Admin Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Admin>> register(@RequestBody @Valid Admin admin) {
        Admin savedAdmin = adminService.registerUser(admin);
        ApiResponse<Admin> response = new ApiResponse<>(true, "Admin registered successfully", savedAdmin);
        return ResponseEntity.ok(response);
    }

    // Admin Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        String token = adminService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        ApiResponse<String> response = new ApiResponse<>(true, "Login successful", token);
        return ResponseEntity.ok(response);
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

