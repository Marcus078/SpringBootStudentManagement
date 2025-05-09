package com.example.StudentManagement.Services;

import com.example.StudentManagement.exception.AdminNotFoundException;
import com.example.StudentManagement.repository.AdminRepository;
import com.example.StudentManagement.Utilities.JwtUtil;
import com.example.StudentManagement.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Admin registerUser(Admin admin) {
        //check if email and password are not null
        if (admin.getEmail() == null || admin.getPassword() == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        }
        // Hash password before saving
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);


    }

    public String loginUser(String email, String password) {
        Optional<Admin> userOpt = adminRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Admin admin = userOpt.get();

            // Compare the password with the hashed password in the database
            if (passwordEncoder.matches(password, admin.getPassword())) {
                // Generate JWT token
                return jwtUtil.generateToken(admin.getEmail());
            } else {
                //exception handling
                throw new AdminNotFoundException("Invalid credentials");
            }
        } else {
            throw new AdminNotFoundException("Admin not found with email: " + email);
        }
    }

}
