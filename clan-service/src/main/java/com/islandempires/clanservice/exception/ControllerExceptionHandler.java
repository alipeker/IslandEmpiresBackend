package com.islandempires.clanservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(CustomRunTimeException.class)
    public ResponseEntity<ErrorMessage> userNamePasswordException(CustomRunTimeException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatusCode(ex.getStatus());
        errorMessage.setMessage(ex.getMessage());
        if(ex.getIdList() != null && ex.getIdList().size() > 0) {
            errorMessage.setMessage(ex.getMessage() + ":" + Arrays.toString(ex.getIdList().toArray()));
        }
        return new ResponseEntity<ErrorMessage>(errorMessage, ex.getHttpStatus());
    }
}
