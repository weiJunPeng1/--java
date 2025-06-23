package com;

public class StudentManagementException extends RuntimeException {
    public StudentManagementException(String message) {
        super(message);
    }

    public StudentManagementException(String message, Throwable cause) {
        super(message, cause);
    }
} 