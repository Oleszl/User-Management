package com.oleszl.employeemanagement.exception.handler;

import com.oleszl.employeemanagement.exception.AgeValidationException;
import com.oleszl.employeemanagement.exception.InvalidDateRangeException;
import com.oleszl.employeemanagement.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        errorDetails.add(new ErrorDetail(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
        return new ResponseEntity<>(new ErrorResponse(errorDetails), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AgeValidationException.class)
    public ResponseEntity<ErrorResponse> handleAgeValidationException(AgeValidationException exception) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        errorDetails.add(new ErrorDetail(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        return new ResponseEntity<>(new ErrorResponse(errorDetails), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErrorResponse> handleAgeValidationException(InvalidDateRangeException exception) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        errorDetails.add(new ErrorDetail(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
        return new ResponseEntity<>(new ErrorResponse(errorDetails), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String message = error.getDefaultMessage();
            errorDetails.add(new ErrorDetail(HttpStatus.BAD_REQUEST.value(), message));
        });
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}



