package com.balaji.orderservice.exception;


import com.balaji.orderservice.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class OrderServiceExceptionHandler {

    // Catch errors thrown during external REST API calls
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ErrorResponseDto> handleRestClientException(RestClientResponseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        
        ErrorResponseDto response = new ErrorResponseDto(
            status.value(),
            "External Service Failure",
            ex.getStatusText(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    // Fallback handler for unchecked runtime exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        ErrorResponseDto response = new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
