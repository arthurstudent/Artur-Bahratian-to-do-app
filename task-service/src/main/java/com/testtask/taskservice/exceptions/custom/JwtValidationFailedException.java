package com.testtask.taskservice.exceptions.custom;

public class JwtValidationFailedException extends RuntimeException{
    public JwtValidationFailedException(String message) {
        super(message);
    }
}
