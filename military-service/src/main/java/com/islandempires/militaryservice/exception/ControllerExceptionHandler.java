package com.islandempires.militaryservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(CustomRunTimeException.class)
    public ResponseEntity<ErrorMessage> customException(CustomRunTimeException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatusCode(ex.getStatus());
        errorMessage.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, ex.getHttpStatus());
    }

}
