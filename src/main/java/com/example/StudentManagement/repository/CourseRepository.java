package com.example.StudentManagement.repository;

import com.example.StudentManagement.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Find a course by its name
    Optional<Course> findByName(String name);
    // Case-insensitive search
    Optional<Course> findByNameIgnoreCase(String name);
}

