package com.example.StudentManagement;

import com.example.StudentManagement.Services.AdminService;
import com.example.StudentManagement.model.Admin;
import com.example.StudentManagement.repository.AdminRepository;
import com.example.StudentManagement.Utilities.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AdminService.
 * Covers registration and login logic including password encoding,
 * JWT generation, and proper exception handling.
 */
public class AdminServiceTest {

    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private AdminService adminService;


     //Set up AdminService with mocked dependencies before each test

    @BeforeEach
    void setup() {
        adminService = new AdminService(adminRepository, passwordEncoder, jwtUtil);
    }


     //Should encode password and save the admin during registration

    @Test
    void registerUser_ShouldEncodePasswordAndSaveAdmin() {
        Admin admin = new Admin();
        admin.setEmail("admin@example.com");
        admin.setPassword("plaintext");
        admin.setFullName("Mikateko");

        when(passwordEncoder.encode("plaintext")).thenReturn("hashed");
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Admin saved = adminService.registerUser(admin);

        assertEquals("hashed", saved.getPassword());
        verify(passwordEncoder).encode("plaintext");
        verify(adminRepository).save(admin);
    }


     //Should return JWT token when login credentials are valid.

    @Test
    void loginUser_ShouldReturnJwtWhenCredentialsAreValid() {
        Admin admin = new Admin();
        admin.setEmail("admin@example.com");
        admin.setPassword("hashedPassword");

        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("plaintext", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin@example.com")).thenReturn("mocked-jwt");

        String token = adminService.loginUser("admin@example.com", "plaintext");

        assertEquals("mocked-jwt", token);
    }

     //Should throw exception when login password is incorrect

    @Test
    void loginUser_ShouldThrowWhenPasswordIsIncorrect() {
        Admin admin = new Admin();
        admin.setEmail("admin@example.com");
        admin.setPassword("hashed");

        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("wrongpass", "hashed")).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                adminService.loginUser("admin@example.com", "wrongpass")
        );
    }


     //Should throw exception when admin email is not found
    @Test
    void loginUser_ShouldThrowWhenAdminNotFound() {
        when(adminRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                adminService.loginUser("unknown@example.com", "any")
        );
    }

    //Should throw IllegalArgumentException when email or password is missing during registration
    @Test
    void registerUser_ShouldThrowIfEmailOrPasswordIsNull() {
        Admin admin = new Admin();
        admin.setEmail(null);
        admin.setPassword(null);

        // Assumes AdminService.registerUser checks for null values
        assertThrows(IllegalArgumentException.class, () ->
                adminService.registerUser(admin)
        );
    }
}
