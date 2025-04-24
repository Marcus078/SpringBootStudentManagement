package com.example.StudentManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Bean validation
    @Column(nullable = false, unique = true, length = 225)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(min = 5, max = 225, message = "Email must be between 5 and 225 characters")
    private String email;

    @Column(nullable = false, length = 225)
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 225, message = "Password must be between 6 and 225 characters")
    private String password;


    @Column(nullable = false, length = 225)
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 225, message = "Full name must be between 2 and 225 characters")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Full name must contain only letters and spaces")
    private String fullName;
}