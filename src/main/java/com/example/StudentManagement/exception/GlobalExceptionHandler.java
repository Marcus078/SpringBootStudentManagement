package com.example.StudentManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<?> handleAdminNotFound(AdminNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of(
                "error", "Admin Not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<?> handleStudentNotFound(StudentNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of(
                "error", "Student Not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<?> handleCourseNotFound(CourseNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of(
                "error", "Course Not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(ModuleNotFoundException.class)
    public ResponseEntity<?> handleModuleNotFound(ModuleNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of(
                "error", "Module Not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }
/*
    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<?> handleJwtTokenException(JwtTokenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "Unauthorized",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now()
        ));
    }

*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(500).body(Map.of(
                "error", "Unexpected Error",
                "message", ex.getMessage()
        ));
    }
}

