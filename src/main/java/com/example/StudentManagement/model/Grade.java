package com.example.StudentManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("grades") // Prevent grades from serializing the student field
    private Student student;

    @ManyToOne
    private Module module;

    @Column(nullable = false)
    @Min(value = 0, message = "Grade must be at least 0.")
    @Max(value = 100, message = "Grade cannot exceed 100.")
    private double grade;

    //Date when the grade was assigned
    private LocalDateTime dateAssigned;

    //Name of the person who updated the grade
    @ManyToOne
    private Admin updatedBy;


    // No-argument constructor
    public Grade() {}

    // Constructor with parameters
    public Grade(Student student, Module module, double grade, LocalDateTime dateAssigned) {
        this.student = student;
        this.module = module;
        this.grade = grade;
        this.dateAssigned = dateAssigned;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public LocalDateTime getDateAssigned() {
        return dateAssigned;
    }

    public void setDateAssigned(LocalDateTime dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

    public Admin getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Admin updatedBy) {
        this.updatedBy = updatedBy;
    }
}
