package com.blubank.doctorappontmentservice.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.NotActiveException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotActiveException.class)
    public ResponseEntity<Object> handleNotAcceptableRequestException(HttpClientErrorException.NotAcceptable exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
