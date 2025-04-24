package com.example.StudentManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 225)
    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
    @Size(min = 2, max = 225, message = "First name must be between 2 and 225 characters.")
    private String firstName;

    // Last name must contain only letters and not be blank
    @Column(nullable = false, length = 225)
    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
    @Size(min = 2, max = 225, message = "Last name must be between 2 and 225 characters.")
    private String lastName;

    // ID Number must be 13 numeric digits
    @Column(nullable = false, unique = true, length = 13)
    @NotBlank(message = "ID Number  is required")
    @Pattern(regexp = "^\\d{13}$", message = "ID Number must be exactly 13 digits.")
    private String idNumber;

    @Column(nullable = false, unique = true, length = 10)
    @NotNull(message = "Student number is required")
    @Pattern(regexp = "^ST\\d{7}$", message = "Student Number must be in the format ST followed by 7 digits.")
    private String studentNumber;

    @Column(nullable = false, unique = true, length = 23)
    @NotBlank(message = "School email is required")
    @Email(message = "Invalid email format")
    private String schoolEmail;

    @Min(value = 1900, message = "Year of enrollment must be after 1900.")
    @Max(value = 2100, message = "Year of enrollment must be before 2100.")
    private int yearOfEnrollment;

    // One student is enrolled in one course
    @ManyToOne
    private Course course;

    // Student can have many grades (linked to modules)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("student")
    private List<Grade> grades = new ArrayList<>();

    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    /*
    // Additional validation for admin
    public void setSchoolEmail(String schoolEmail) {
        if (schoolEmail.contains("<") || schoolEmail.contains(">") || schoolEmail.contains("'")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.schoolEmail = schoolEmail;
    }

    // ID number validation
    public void setIdNumber(String idNumber) {
        if (!idNumber.matches("^\\d{13}$")) {
            throw new IllegalArgumentException("ID Number must be exactly 13 digits.");
        }
        this.idNumber = idNumber;
    }

     */
}
