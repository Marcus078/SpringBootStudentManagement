package com.example.StudentManagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity  // Marks this class as a database entity
@Table(name = "admin")  // Maps this entity to the "users" table
@Getter @Setter  @NoArgsConstructor @AllArgsConstructor
public class Admin {
    @Id  // Specifies the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment ID
    private Long id;

    @Column(nullable = false, unique = true, length = 225)  // Unique, required email
    private String email;

    @Column(nullable = false, length = 225)  // Required password
    private String password;

    @Column(nullable = false, length = 225)  // Required full name
    private String fullName;


}
