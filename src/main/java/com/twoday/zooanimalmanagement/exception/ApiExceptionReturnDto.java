package com.twoday.zooanimalmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApiExceptionReturnDto {
    private Timestamp timestamp;
    private HttpStatus status;
    private String error;
}
