package com.example.StudentManagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the course must be unique, not blank, and within the allowed length
    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Course name is required")  // Ensure the field is not blank
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")  // Ensure length constraints
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Course name must contain only letters, numbers, and spaces")  // Prevent XSS (no special chars)
    private String name;


    //private int year;
    @Min(value = 1900, message = "Year must be after 1900.")
    @Max(value = 2100, message = "Year must be before 2100")
    private int year;

    // One course can have many modules; this relation is bidirectional
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference  // Prevent infinite recursion by managing this side of the relationship
    private List<Module> modules = new ArrayList<>();


}
