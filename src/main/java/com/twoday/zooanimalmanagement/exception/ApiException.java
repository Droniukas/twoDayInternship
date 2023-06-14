package com.twoday.zooanimalmanagement.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApiException extends RuntimeException {
    HttpStatus status;
    String message;
}
