package com.testtask.taskservice.exceptions.handler;

import com.testtask.taskservice.exceptions.custom.DataMappingException;
import com.testtask.taskservice.exceptions.custom.FileProcessingException;
import com.testtask.taskservice.exceptions.custom.JwtValidationFailedException;
import com.testtask.taskservice.model.dto.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .filter(e -> e.getDefaultMessage() != null)
                .collect(Collectors.toMap(FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        ApiResponse<Map<String, String>> mapApiResponse = ApiResponse.failResponse(errors);
        return ResponseEntity.badRequest().body(mapApiResponse);
    }

    @ExceptionHandler(JwtValidationFailedException.class)
    public ResponseEntity<ApiResponse<String>> handleJwtValidationFailedException(JwtValidationFailedException ex) {
        ApiResponse<String> apiResponse = ApiResponse.failResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiResponse<String> apiResponse = ApiResponse.failResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ApiResponse<String>> handleFileProcessingException(FileProcessingException ex) {
        ApiResponse<String> apiResponse = ApiResponse.failResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataMappingException.class)
    public ResponseEntity<ApiResponse<String>> handleDataMappingException(DataMappingException ex) {
        ApiResponse<String> apiResponse = ApiResponse.failResponse(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleBaseException(Exception ex) {
        log.error("Exception", ex);
        ApiResponse<String> apiResponse = ApiResponse.failResponse("Something went wrong");
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        log.error("HttpMessageNotReadableException", ex);
        ApiResponse<String> apiResponse = ApiResponse.failResponse("Unable to parse request body, please check passed arguments");
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation error: ", ex);
        String userFriendlyMessage = "Data already exists or unique restrictions are violated";
        ApiResponse<String> apiResponse = ApiResponse.failResponse(userFriendlyMessage);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
