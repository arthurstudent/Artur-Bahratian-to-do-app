package com.testtask.taskservice.exceptions.custom;

public class FileProcessingException extends RuntimeException{
    public FileProcessingException(String message) {
        super(message);
    }
}
