package com.example.StudentManagement.repository;


import com.example.StudentManagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // Find a student by their student number
    Optional<Student> findByStudentNumber(String studentNumber);

    Optional<Student> findById(Long studentId);  // This is already included in JpaRepository

   //find student by first or last name
    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    //find student by first and last name
    List<Student> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Student> findByCourse_Name(String courseName);

    //track the highest student number ever generated
    @Query("SELECT MAX(s.studentNumber) FROM Student s")
    String findMaxStudentNumber();

}

