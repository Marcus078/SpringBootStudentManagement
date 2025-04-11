package com.example.StudentManagement.exception;

public class StudentNotFoundException  extends  RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
