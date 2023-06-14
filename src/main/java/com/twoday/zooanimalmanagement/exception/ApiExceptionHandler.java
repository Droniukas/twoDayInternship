package com.twoday.zooanimalmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;


@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionReturnDto> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Date date = new java.util.Date();
        return new ResponseEntity<>(new ApiExceptionReturnDto(new Timestamp(date.getTime()),
                HttpStatus.BAD_REQUEST,
                Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionReturnDto> handleCustomExceptions(ApiException exception) {
        Date date = new java.util.Date();
        return new ResponseEntity<>(new ApiExceptionReturnDto(new Timestamp(date.getTime()),
                exception.status,
                exception.getMessage()),
                exception.status);
    }
}
