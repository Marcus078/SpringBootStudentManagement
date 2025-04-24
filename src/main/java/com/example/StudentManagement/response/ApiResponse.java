package com.example.StudentManagement.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

}
