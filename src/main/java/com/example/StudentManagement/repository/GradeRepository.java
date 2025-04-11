package com.example.StudentManagement.repository;

import com.example.StudentManagement.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}

